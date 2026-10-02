const container = document.getElementById('container');
const loading = document.querySelector('.loading');

var hasNextToken = true;
var resumeToken = '';
const size = 10; // use it if you want (in this code is not used)

getPost();

function infniteScroll() {
    if (hasNextToken) {
        window.addEventListener('scroll', iscroll);
    } else {
        alert("You've scrolled through all the data ...");
    }
}

function showLoading() {
    window.removeEventListener('scroll', iscroll);
    loading.classList.add('show');
    setTimeout(getPost, 1000)
}

function iscroll() {
    const {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    console.log({scrollTop, scrollHeight, clientHeight});

    if (clientHeight + scrollTop >= scrollHeight - 5) {
        showLoading();
    }
}

async function getPost() {

    const postResponse = await fetch(`/authors?resumeToken=${encodeURIComponent(resumeToken)}`);
    const data = await postResponse.json();

    resumeToken = data.nextToken;    
    hasNextToken = data.hasNext;

    infniteScroll();
    addDataToDOM(data.content);
}

function addDataToDOM(data) {
    for (var i = 0; i < data.length; i++) {

        const postElement = document.createElement('div');

        postElement.classList.add('orders');
        postElement.innerHTML = `
		<h2 class="title">${data[i].id}</h2>
		<p class="text">Created at: ${data[i].createdAt}</p>	                
                <div class="user-info">
                    <span>Name: ${data[i].name}</span>
                </div>        
                <div class="user-info">
                    <span>Genre: ${data[i].genre}</span>
                </div>		
                <div class="user-info">
                    <span>Age: ${data[i].age}</span>
                </div>		 
	`;

        container.appendChild(postElement);
        loading.classList.remove('show');
    }
}