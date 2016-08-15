var stomp = Stomp.over(new SockJS('/ws'));

stomp.connect('guest', 'guest', function() {
    stomp.subscribe("/app/todoes", initialMsg);
    stomp.subscribe("/topic/todoes", updateMsg);
});

var todoes = []
function initialMsg(message) {
    todoes = JSON.parse(message.body);
    showData()
}

function updateMsg(message) {
    var patches = JSON.parse(message.body);
    jsonpatch.apply(todoes, patches);
    showData()
}

function showData(){
    var ul = document.querySelector('ul')
    ul.innerHTML = '';
    todoes.forEach(e => {
        ul.appendChild(document.createElement("li")).outerHTML = `<li data-list class="todo-item">
                <input type="hidden" name="id" value="${e.id}"/>
                <input class="toggle" type="checkbox" name="complete" value="${e.complete}"/>
                <input type="text" name="description" value="${e.description}"/>
                <span class="remove" onclick="deleteById(this)">&times;</span>
            </li>`
    })
}

function deleteById(e){
    var id = e.parentNode.querySelector('input[name=id]').value
    fetch("/todoes/" + id, {method: "DELETE"})
}

function create(){
    var description = document.forms[0].description.value;
    fetch("/todoes",
    {
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({description: description, complete: false})
    })
}

function completeAll(){}