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
                <td>${event.time.slice(0, 5)}</td>
                <td>${event.location}</td>
                <td>${event.category.name}</td>
                <td>
                    <a href="event-form.html?id=${event.id}">Editar</a>
                    <button onclick="deleteEvent(${event.id})">Excluir</button>
                </td>
            </tr>
        `;
  }
}

async function deleteEvent(id) {
  if (!confirm("Deseja excluir este evento?")) return;

  if (await api("DELETE", `/events/${id}`)) {
    listEvents();
  }
}

document.getElementById("filter-form").addEventListener("submit", (event) => {
  event.preventDefault();
  listEvents();
});

loadCategories("filter-category");
listEvents();
