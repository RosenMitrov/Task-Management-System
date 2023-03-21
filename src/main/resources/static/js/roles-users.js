let loadRolesBtn = document.getElementById("loadRoles");

loadRolesBtn.addEventListener('click', loadRoles);

let roleCntr = document.getElementById('roles-container');
let tableCntr = document.getElementById('table-container');

function loadRoles() {
    roleCntr.innerHTML = ''
    tableCntr.hidden = true;

    fetch("http://localhost:8080/api/roles")
        .then(response => response.json())
        .then(json => json.forEach(role => {

            let row = document.createElement('tr');
            row.className = 'mytrowconfig'
            let roleName = document.createElement('td');
            let usersCount = document.createElement('td');
            let btnWrap = document.createElement('td');
            btnWrap.className = 'text-center'
            let btnCol = document.createElement('button')

            btnCol.className = 'btn btn-admin-details'
            btnCol.innerText = 'Show users'
            btnCol.dataset.id = role.id;

            btnWrap.appendChild(btnCol)
            btnCol.addEventListener("click", getRoleId)

            roleName.textContent = role.role;
            usersCount.textContent = role.countUsers;

            row.appendChild(roleName)
            row.appendChild(usersCount)
            row.appendChild(btnWrap)
            roleCntr.appendChild(row)
        }))
}

function getRoleId(event) {
    let roleId = event.target.dataset.id;
    showAllUsersWithRoleId(roleId)
}

let userCntr = document.getElementById('users-container');

function showAllUsersWithRoleId(roleId) {
    userCntr.innerHTML = ''
    tableCntr.hidden = false;

    fetch(`http://localhost:8080/api/user-with-role/${roleId}`)
        .then(response => response.json())
        .then(json => json.forEach(user => {

                let row = document.createElement('tr');
                row.className = 'mytrowconfig'
                let userName = document.createElement('td');
                let userAge = document.createElement('td');
                let userDep = document.createElement('td');

                userName.textContent = user.username;
                userAge.textContent = user.age;
                userDep.textContent = user.department;

                row.appendChild(userName);
                row.appendChild(userAge);
                row.appendChild(userDep);

                userCntr.appendChild(row);
            }
        ))
}
