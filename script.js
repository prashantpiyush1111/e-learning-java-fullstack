fetch("http://localhost:8083/students")
  .then(response => response.json())
  .then(students => {
    console.log("Students from backend:", students);

    const list = document.getElementById("studentList");
    list.innerHTML = "";

    students.forEach(s => {
      const li = document.createElement("li");
      li.textContent = s.name + " | " + s.email;
      list.appendChild(li);
    });
  })
  .catch(error => {
    console.error("Error:", error);
  });

  function addStudent() {
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  fetch("http://localhost:8083/students", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      name: name,
      email: email,
      password: password
    })
  })
  .then(response => response.json())
  .then(data => {
    alert("Student Added Successfully");

    // clear form
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";

    // reload list
    location.reload();
  })
  .catch(error => {
    console.error("Error:", error);
  });
}

function loginStudent() {
  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;
  const msg = document.getElementById("loginMsg");

  fetch("http://localhost:8083/students/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      email: email,
      password: password
    })
  })
  .then(res => res.text())   // 🔥 IMPORTANT CHANGE
  .then(data => {
    if (data.startsWith("Login Successful")) {
      msg.style.color = "green";
      msg.innerText = data;
    } else {
      msg.style.color = "red";
      msg.innerText = "Invalid email or password";
    }
  })
  .catch(err => {
    msg.style.color = "red";
    msg.innerText = "Server error";
    console.error(err);
  });
}
