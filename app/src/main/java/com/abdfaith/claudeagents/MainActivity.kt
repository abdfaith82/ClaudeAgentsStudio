package com.abdfaith.claudeagents

import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class AgentStep(
    val id: Int,
    val name: String,
    val role: String,
    val status: String,
    val output: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFD97757),
                    secondary = Color(0xFF38BDF8),
                    background = Color(0xFF0F172A),
                    surface = Color(0xFF1E293B)
                )
            ) {
                MultiAgentStudioScreen()
            }
        }
    }
}

@Composable
fun MultiAgentStudioScreen() {
    val scope = rememberCoroutineScope()
    var claudeKey by remember { mutableStateOf("") }
    var githubToken by remember { mutableStateOf("") }
    var githubUser by remember { mutableStateOf("abdfaith82") }
    var targetRepo by remember { mutableStateOf("Claude-agents") }
    var taskPrompt by remember { mutableStateOf("Build a Python autonomous code reviewer agent") }
    var isRunning by remember { mutableStateOf(false) }

    var agents by remember {
        mutableStateOf(
            listOf(
                AgentStep(1, "Architect Agent", "System & File Structure Planner", "Idle", "Waiting for task..."),
                AgentStep(2, "Coder Agent", "Production Code Generator", "Idle", "Waiting for Architect..."),
                AgentStep(3, "GitHub Deployer Agent", "Auto Repo & File Pusher", "Idle", "Waiting for Coder...")
            )
        )
    }

    fun updateAgent(id: Int, status: String, output: String) {
        agents = agents.map { if (it.id == id) it.copy(status = status, output = output) else it }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Claude Multi-Agent Studio",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Architect -> Coder -> GitHub Auto-Deployer Pipeline",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("API & GitHub Configuration", fontWeight = FontWeight.SemiBold, color = Color.White)
                        OutlinedTextField(
                            value = claudeKey,
                            onValueChange = { claudeKey = it },
                            label = { Text("Anthropic API Key (Optional - Offline fallback)") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = githubUser,
                                onValueChange = { githubUser = it },
                                label = { Text("GitHub User") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = targetRepo,
                                onValueChange = { targetRepo = it },
                                label = { Text("Repo Name") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        OutlinedTextField(
                            value = githubToken,
                            onValueChange = { githubToken = it },
                            label = { Text("GitHub Classic PAT (ghp_...)") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = taskPrompt,
                    onValueChange = { taskPrompt = it },
                    label = { Text("Mission Prompt for Linked Agents") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (!isRunning) {
                            isRunning = true
                            scope.launch {
                                updateAgent(1, "Running...", "Designing architecture for: $taskPrompt")
                                val archPlan = callClaudeOrSimulate(
                                    apiKey = claudeKey,
                                    systemRole = "You are a Senior Software Architect. Output a concise module specification.",
                                    userPrompt = taskPrompt,
                                    fallbackText = "Architecture Spec:\n- Module: agents/autonomous_agent.py\n- Class: AutonomousTaskAgent\n- Pipeline: Input -> Analyze -> Execute"
                                )
                                updateAgent(1, "Completed", archPlan)

                                updateAgent(2, "Running...", "Generating production code from Architect spec...")
                                val generatedCode = callClaudeOrSimulate(
                                    apiKey = claudeKey,
                                    systemRole = "You are a Senior Python Developer. Output clean runnable Python code only.",
                                    userPrompt = "Write Python code based on this spec:\n$archPlan",
                                    fallbackText = "class AutonomousTaskAgent:\n    def __init__(self):\n        self.ready = True\n\n    def run(self, task: str) -> str:\n        return f'Executed by Linked Agent Pipeline: {task}'\n\nif __name__ == '__main__':\n    print(AutonomousTaskAgent().run('$taskPrompt'))\n"
                                )
                                updateAgent(2, "Completed", generatedCode)

                                updateAgent(3, "Running...", "Pushing generated code to GitHub ($githubUser/$targetRepo)...")
                                val deployResult = pushToGitHubRepo(
                                    user = githubUser.trim(),
                                    repo = targetRepo.trim(),
                                    token = githubToken.trim(),
                                    filePath = "agents/generated_pipeline_agent.py",
                                    content = generatedCode
                                )
                                updateAgent(3, "Completed", deployResult)
                                isRunning = false
                            }
                        }
                    },
                    enabled = !isRunning,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(if (isRunning) "Executing Linked Agents..." else "Run Multi-Agent Pipeline & Push")
                }
            }

            items(agents) { agent ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${agent.id}. ${agent.name}", fontWeight = FontWeight.Bold, color = Color.White)
                                Text(agent.role, fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(
                                text = agent.status,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (agent.status) {
                                    "Completed" -> Color(0xFF4ADE80)
                                    "Running..." -> Color(0xFFFACC15)
                                    else -> Color.LightGray
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF090D16), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = agent.output,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }
        }
    }
}

suspend fun callClaudeOrSimulate(
    apiKey: String,
    systemRole: String,
    userPrompt: String,
    fallbackText: String
): String = withContext(Dispatchers.IO) {
    if (apiKey.isBlank()) {
        return@withContext "[Offline Simulation Mode]\n$fallbackText"
    }
    try {
        val url = URL("https://api.anthropic.com/v1/messages")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("x-api-key", apiKey)
            setRequestProperty("anthropic-version", "2023-06-01")
            setRequestProperty("content-type", "application/json")
            doOutput = true
            connectTimeout = 20000
            readTimeout = 20000
        }
        val payload = JSONObject().apply {
            put("model", "claude-3-5-sonnet-latest")
            put("max_tokens", 800)
            put("system", systemRole)
            put("messages", JSONArray().put(JSONObject().apply {
                put("role", "user")
                put("content", userPrompt)
            }))
        }
        OutputStreamWriter(conn.outputStream).use { it.write(payload.toString()) }
        val responseText = if (conn.responseCode in 200..299) {
            conn.inputStream.bufferedReader().readText()
        } else {
            val err = conn.errorStream?.bufferedReader()?.readText() ?: "HTTP ${conn.responseCode}"
            return@withContext "[Claude API Error]: $err"
        }
        val json = JSONObject(responseText)
        json.getJSONArray("content").getJSONObject(0).getString("text")
    } catch (e: Exception) {
        "[Connection Error]: ${e.localizedMessage}"
    }
}

suspend fun pushToGitHubRepo(
    user: String,
    repo: String,
    token: String,
    filePath: String,
    content: String
): String = withContext(Dispatchers.IO) {
    if (token.isBlank()) {
        return@withContext "[Simulated GitHub Push] Ready to push '$filePath' to https://github.com/$user/$repo (Add Classic PAT to push live)."
    }
    try {
        val createUrl = URL("https://api.github.com/user/repos")
        (createUrl.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Authorization", "token $token")
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            OutputStreamWriter(outputStream).use {
                it.write(JSONObject().put("name", repo).put("private", false).toString())
            }
            responseCode
        }

        val apiUrl = "https://api.github.com/repos/$user/$repo/contents/$filePath"
        var existingSha: String? = null
        val getConn = (URL(apiUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Authorization", "token $token")
        }
        if (getConn.responseCode == 200) {
            val existingJson = JSONObject(getConn.inputStream.bufferedReader().readText())
            existingSha = existingJson.optString("sha", null)
        }

        val putConn = (URL(apiUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "PUT"
            setRequestProperty("Authorization", "token $token")
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
        }
        val b64Content = Base64.encodeToString(content.toByteArray(), Base64.NO_WRAP)
        val body = JSONObject().apply {
            put("message", "Automated push from Claude Agents Android Studio")
            put("content", b64Content)
            if (!existingSha.isNullOrEmpty()) put("sha", existingSha)
        }
        OutputStreamWriter(putConn.outputStream).use { it.write(body.toString()) }
        if (putConn.responseCode in 200..299) {
            "Successfully deployed '$filePath' -> https://github.com/$user/$repo/blob/main/$filePath"
        } else {
            val err = putConn.errorStream?.bufferedReader()?.readText() ?: "HTTP ${putConn.responseCode}"
            "[GitHub API Error]: $err"
        }
    } catch (e: Exception) {
        "[Deploy Error]: ${e.localizedMessage}"
    }
}
