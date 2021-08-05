(function () {
    var API_URL = 'http://localhost:8080/api';
    var TODO_API_URL = API_URL + "/todos";
    var todoText = document.getElementById('todoText');
    fetch(TODO_API_URL)
        .then(processOkResponse)
        .then(function (todos) { return todos.forEach(createNewTodo); });
    document.getElementById('addTodo').addEventListener('click', function (event) {
        event.preventDefault();
        fetch(TODO_API_URL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text: todoText.value })
        })
            .then(processOkResponse)
            .then(createNewTodo)
            .then(function () { return todoText.value = ''; })["catch"](console.warn);
    });
    function createNewTodo(todo) {
        var label = document.createElement('label');
        label.classList.add('pure-checkbox');
        var checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.checked = todo.done;
        checkbox.addEventListener('click', function (event) {
            event.preventDefault();
            fetch(TODO_API_URL + "/" + todo.id, { method: 'PUT' })
                .then(processOkResponse)
                .then(function (updatedTodo) { return checkbox.checked = updatedTodo.done; })["catch"](console.warn);
        });
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(" " + todo.text));
        document.getElementById('allTodos').appendChild(label);
    }
    initWelcomeForm();
    function initWelcomeForm() {
        var CODE_TO_EMOJI = {
            'pl': '🇵🇱',
            'en': '🇺🇸',
            'es': '🇪🇸',
            'de': '🇩🇪'
        };
        fetch(API_URL + "/langs")
            .then(processOkResponse)
            .then(function (langArr) {
            document.getElementById('langs').innerHTML = langArr.map(function (lang) { return "\n              <label class=\"pure-radio\">\n                <input type=\"radio\" name=\"lang\" value=\"" + lang.id + "\">\n                " + CODE_TO_EMOJI[lang.code] + "\n              </label>\n          "; }).join('\n');
            initWelcomeFormClick();
        });
    }
    function initWelcomeFormClick() {
        var welcomeForm = document.getElementById('welcomeForm');
        var name = welcomeForm.elements.namedItem("name");
        var lang = welcomeForm.elements.namedItem("lang");
        document.getElementById('btn').addEventListener('click', function (event) {
            event.preventDefault();
            var formObj = {
                name: name.value,
                lang: lang.value
            };
            fetch(API_URL + "?" + new URLSearchParams(formObj))
                .then(function (response) { return response.text(); })
                .then(function (text) {
                document.getElementById('welcome').innerHTML = "\n                <h1>" + text + "</h1>\n            ";
                welcomeForm.remove();
                document.getElementById('todoForm').style.display = 'block';
            });
        });
    }
    function processOkResponse(response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error("Status not 200 (" + response.status + ")");
    }
})();
