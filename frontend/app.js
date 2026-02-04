const API_BASE = "http://localhost:8080/api/study-tasks";

/* ---------- Add / Update Task ---------- */
function addTask() {
    const userId = document.getElementById("userId").value.trim();
    if (!userId) {
        alert("User ID required");
        return;
    }

    localStorage.setItem("userId", userId);

    const taskId = document.getElementById("taskId").value;
    let priority = Number(document.getElementById("priority").value);

// Clamp priority between 1 and 3
if (priority < 1) priority = 1;
if (priority > 3) priority = 3;

    const data = {
        userId: userId,
        subject: document.getElementById("subject").value,
        topic: document.getElementById("topic").value,
        dueDate: document.getElementById("dueDate").value,
        priority: priority
    };

    const method = taskId ? "PUT" : "POST";
    const url = taskId ? `${API_BASE}/${taskId}` : API_BASE;

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert(taskId ? "Task updated" : "Task added");
        resetForm();
        loadTasks();
    })
    .catch(() => alert("Operation failed"));
}

/* ---------- Load Tasks ---------- */
function loadTasks() {
    const userId = document.getElementById("userId").value;
    if (!userId) return;

    fetch(`${API_BASE}/${userId}`)
        .then(res => res.json())
        .then(tasks => {
            const ul = document.getElementById("taskList");
            ul.innerHTML = "";

            if (tasks.length === 0) {
                ul.innerHTML = "<li>No tasks yet</li>";
                return;
            }

            tasks.forEach(task => {
                const li = document.createElement("li");
                li.className = "task-item";

                const statusClass = task.status.toLowerCase().replace("_", "-");

                li.innerHTML = `
                    <div>
                        <strong>${task.subject}</strong> - ${task.topic}
                    </div>

                    <span class="status ${statusClass}">
                        ${task.status}
                    </span>

                    <div class="task-actions">
                        <button class="edit" onclick="editTask('${task.id}')">Edit</button>
                        <button class="toggle" onclick="toggleStatus('${task.id}')">Toggle</button>
                        <button class="delete" onclick="deleteTask('${task.id}')">Delete</button>
                    </div>
                `;

                ul.appendChild(li);
            });
        });
}

/* ---------- Edit ---------- */
function editTask(id) {
    fetch(`${API_BASE}/task/${id}`)
        .then(res => res.json())
        .then(task => {
            document.getElementById("taskId").value = task.id;
            document.getElementById("subject").value = task.subject;
            document.getElementById("topic").value = task.topic;
            document.getElementById("dueDate").value = task.dueDate;
            document.getElementById("priority").value = task.priority;

            // 🔥 EDIT MODE UX
            document.querySelector("section").classList.add("edit-mode");
            document.getElementById("submitBtn").innerText = "Update Task";
        });
}
/* ---------- Toggle Status ---------- */
function toggleStatus(id) {
    fetch(`${API_BASE}/${id}/status`, { method: "PATCH" })
        .then(() => loadTasks());
}

/* ---------- Delete ---------- */
function deleteTask(id) {
    if (!confirm("Delete this task?")) return;
    fetch(`${API_BASE}/${id}`, { method: "DELETE" })
        .then(() => loadTasks());
}

/* ---------- Helpers ---------- */
function resetForm() {
    document.getElementById("taskId").value = "";
    document.getElementById("subject").value = "";
    document.getElementById("topic").value = "";
    document.getElementById("dueDate").value = "";
    document.getElementById("priority").value = "1";

    // 🔥 EXIT EDIT MODE
    document.querySelector("section").classList.remove("edit-mode");
    document.getElementById("submitBtn").innerText = "Add Task";
}

/* ---------- On Load ---------- */
window.onload = function () {
    const savedUserId = localStorage.getItem("userId");
    if (savedUserId) {
        document.getElementById("userId").value = savedUserId;
        loadTasks();
    }
};

