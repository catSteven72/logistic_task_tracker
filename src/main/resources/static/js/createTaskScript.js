document.addEventListener('DOMContentLoaded', function() {
        let placeIndex = 0;
        const addPlaceBtn = document.getElementById('addplace');

        function addSubtask(placeDiv, placeIdx) {
            const subtaskContainer = placeDiv.querySelector('.subtasksContainer');
            const subtasks = subtaskContainer.children.length-1;
            const inputHtml = `
                <div class="subtask">
                    Subtask Name: <input type="text" name="places[${placeIdx}].subtasks[${subtasks}].name" required/>
                    Subtask Description: <input type="text" name="places[${placeIdx}].subtasks[${subtasks}].description"/>
                    <button type="button" onclick="this.parentNode.remove()">Remove Subtask</button>
                </div>
            `;
            subtaskContainer.insertAdjacentHTML('beforeend', inputHtml);
        }

        addPlaceBtn.addEventListener('click', function() {
            const thisPlaceIndex = placeIndex;
            const placesContainer = document.getElementById('placesContainer');
            const placeDiv = document.createElement('div');
            placeDiv.className = 'place';
            placeDiv.innerHTML = `
                <div>
                    City: <input type="text" name="places[${placeIndex}].city" required/>
                    Country: <input type="text" name="places[${placeIndex}].country" required/>
                    Forwarder: <input type="text" name="places[${placeIndex}].forwarder" required/>
                    Index: <input type="number" name="places[${placeIndex}].index" required/>
                    <button type="button" onclick="this.parentNode.remove()">Remove place</button>
                </div>
                <div class="subtasksContainer">
                    <button type="button">Add Subtask</button>
                </div>
            `;
            placesContainer.appendChild(placeDiv);
            placeDiv.querySelector('.subtasksContainer button').addEventListener('click', function() {
                addSubtask(placeDiv, thisPlaceIndex);
            });
            placeIndex++;
        });
    });