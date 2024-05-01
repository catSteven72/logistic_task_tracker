let csrfToken = null;

    async function initializeCsrfToken() {
        try {
            const response = await fetch('/csrf-token', { method: 'GET' });
            const result = await response.json();
            return result.token;
        } catch (error) {
            console.error('Error fetching CSRF Token:', error);
            return null;
        }
    }

    async function handleToken() {
        csrfToken = await initializeCsrfToken();
        if (csrfToken) {
            console.log('Retrieved CSRF Token');
        } else {
            console.log('No token could be retrieved.');
        }
    }


    handleToken();

    function toggleStatus(element, event) {

        event.stopPropagation()

        const itemId = element.getAttribute('data-id');
        const entityType = element.getAttribute('data-entity');
        const statusField = element.getAttribute('data-status-field');
        const currentStatus = element.classList.contains('active');
        const newStatus = !currentStatus;

        const endpointUrl = `/update${entityType}${statusField}Status`;

        fetch (endpointUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': csrfToken
            },
            body: `id=${itemId}&is${statusField}=${newStatus}`
        })
        .then(response => response.text().then(text => ({ text, ok: response.ok})))
        .then(({ text, ok }) => {
            if (ok) {
                element.classList.toggle('active', newStatus);
                console.log('Status update response:', text);
            } else {
                throw new Error(text);
            }
        })
        .catch(error => {
            console.error('Failed to update status:', error);
        });
    }

    function togglePlaces(element) {
        var places = element.querySelector('.places');
        if (places.style.display === 'grid') {
            places.style.display = 'none';
        } else {
            places.style.display = 'grid';
        }
    }

    function toggleColor(event, element) {
        event.stopPropagation();
        element.classList.toggle('active');
    }
