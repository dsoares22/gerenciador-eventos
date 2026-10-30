async function listEvents() {
  const params = new URLSearchParams();

  const title = document.getElementById("filter-title").value;
  const categoryId = document.getElementById("filter-category").value;
  const startDate = document.getElementById("filter-start").value;
  const endDate = document.getElementById("filter-end").value;

  if (title) params.append("title", title);
  if (categoryId) params.append("categoryId", categoryId);
  if (startDate) params.append("startDate", startDate);
  if (endDate) params.append("endDate", endDate);

  const events = await api("GET", `/events?${params}`);
  if (!events) return;

  const table = document.getElementById("events");
  table.innerHTML = "";

  for (const event of events) {
    table.innerHTML += `
      <tr>
        <td>${event.title}</td>
        <td>${event.date.split("-").reverse().join("/")}</td>
        <td>${event.time}</td>
        <td>${event.location}</td>
        <td>${event.category.name}</td>
        <td>
          <button onclick="editEvent(${event.id})">Editar</button>
          <button onclick="deleteEvent(${event.id})">Excluir</button>
        </td>
      </tr>`;
  }
}

async function saveEvent(e) {
  e.preventDefault();

  const id = document.getElementById("id").value;
  const event = {
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    date: document.getElementById("date").value || null,
    time: document.getElementById("time").value || null,
    location: document.getElementById("location").value,
    categoryId: document.getElementById("category").value || null,
  };

  const result = id
    ? await api("PUT", `/events/${id}`, event)
    : await api("POST", "/events", event);
  if (!result) return;

  document.getElementById("form").reset();
  document.getElementById("id").value = "";
  listEvents();
}

async function editEvent(id) {
  const event = await api("GET", `/events/${id}`);
  if (!event) return;

  document.getElementById("id").value = event.id;
  document.getElementById("title").value = event.title;
  document.getElementById("description").value = event.description || "";
  document.getElementById("date").value = event.date;
  document.getElementById("time").value = event.time;
  document.getElementById("location").value = event.location;
  document.getElementById("category").value = event.category.id;
}

async function deleteEvent(id) {
  if (await api("DELETE", `/events/${id}`)) {
    listEvents();
  }
}

document.getElementById("form").addEventListener("submit", saveEvent);
document.getElementById("form").addEventListener("reset", () => {
  document.getElementById("id").value = "";
});
document.getElementById("filters").addEventListener("submit", (e) => {
  e.preventDefault();
  listEvents();
});
document.getElementById("filters").addEventListener("reset", () => {
  setTimeout(listEvents);
});

loadCategories("category");
loadCategories("filter-category");
listEvents();
