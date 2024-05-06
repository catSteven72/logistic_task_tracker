let csrfToken = null;

async function initializeCsrfToken() {
        try {
            const response = await fetch('/csrf-token', { method: 'GET' });
            const result = await response.json();
            return result.token;
        } catch (error) {
            return null;
        }
    }

async function handleToken() {
    csrfToken = await initializeCsrfToken();
}


handleToken();

function updateBooking() {
    let formData = {
        bookingId: document.querySelector('input[name="bookingId"]').value,
        bookingName: document.querySelector('input[name="bookingName"]').value,
        bookingDescription: document.querySelector('input[name="bookingDescription').value,
        places: Array.from(document.querySelectorAll('.place')).map(place => {
            let placeData = {
                            id: place.querySelector('input[name$=".id"]').value,
                            city: place.querySelector('input[name$=".city"]').value,
                            country: place.querySelector('input[name$=".country"]').value,
                            forwarder: place.querySelector('input[name$=".forwarder"]').value,
                            index: place.querySelector('input[name$=".index"]').value,
                            subtasks: Array.from(place.querySelectorAll('.subtask')).map(subtask => {
                                return {
                                    id: subtask.querySelector('input[name$=".id"]').value,
                                    name: subtask.querySelector('input[name$=".name"]').value,
                                    description: subtask.querySelector('textarea[name$=".description"]').value
                                };
                            })
            };
            return placeData;
        })
    }

    fetch('/update-booking', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(formData)
    }).then(response => response.json())
      .then(data => alert('Booking updated!'))
      .catch(error => {
          alert('Error updating booking. Check console for more details.');
      });
}

function addPlace() {
    const placesContainer = document.getElementById('placesContainer');
    const placeIndex = document.querySelectorAll('.place').length;
    const newPlaceHtml = `
        <div class="place">
            <input type="hidden" name="places[${placeIndex}].id" value="0"/>
            <label>City:</label>
            <input type="text" name="places[${placeIndex}].city" value=""/>
            <label>Country:</label>
            <input type="text" name="places[${placeIndex}].country" value=""/>
            <input type="text" name="places[${placeIndex}].forwarder" value=""/>
            <input type="number" name="places[${placeIndex}].index" value="${placeIndex}"/>

            <h4>Subtasks:</h4>
            <div class="subtasks-container"></div>
            <button type="button" onclick="addSubtask(${placeIndex})">Add Subtask</button>
            <button type="button" class="remove-button" onclick="removePlace(this)">Remove Place</button>
        </div>`;
    placesContainer.insertAdjacentHTML('beforeend', newPlaceHtml);
}

function removePlace(button) {
    button.parentNode.remove();
}

function addSubtask(button) {
    const placeIndex = button.getAttribute('data-index');

    const placeDiv = document.querySelectorAll('.place')[placeIndex];
    const subtasksContainer = placeDiv.querySelector('.subtasks-container');
    const subtaskIndex = subtasksContainer.children.length;
    const newSubtaskHtml = `
        <div class="subtask">
            <input type="hidden" name="places[${placeIndex}].subtasks[${subtaskIndex}].id" value="0"/>
            <label>Name:</label>
            <input type="text" name="places[${placeIndex}].subtasks[${subtaskIndex}].name" value=""/>
            <label>Description:</label>
            <textarea name="places[${placeIndex}].subtasks[${subtaskIndex}].description"></textarea>
            <button type="button" class="remove-button" onclick="removeSubtask(this)">Remove Subtask</button>
        </div>`;
    subtasksContainer.insertAdjacentHTML('beforeend', newSubtaskHtml);
}

function removeSubtask(button) {
    button.parentNode.remove();
}

function markBookingCompleted() {
    const bookingId = document.querySelector('input[name="bookingId"]').value;

    fetch(`/mark-booking-completed/${bookingId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to mark booking as completed');
        }
        return response.json();
    }).then(data => {
        alert('Booking marked as completed!');
    }).catch(error => {
        alert('Failed to mark booking as completed');
    });
}