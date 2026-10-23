const eventId = new URLSearchParams(window.location.search).get("id");

async function loadEvent() {
  const event = await api("GET", `/events/${eventId}`);
  if (!event) return;

  document.getElementById("form-title").textContent = "Editar Evento";
  document.getElementById("title").value = event.title;
  document.getElementById("description").value = event.description || "";
  document.getElementById("date").value = event.date;
  document.getElementById("time").value = event.time.slice(0, 5);
  document.getElementById("location").value = event.location;
  document.getElementById("category").value = event.category.id;
}
async function saveEvent(e) {
  e.preventDefault();

  const categoryId = document.getElementById("category").value;
  const event = {
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    date: document.getElementById("date").value || null,
    time: document.getElementById("time").value || null,
    location: document.getElementById("location").value,
    category: categoryId ? { id: Number(categoryId) } : null,
  };

  const result = eventId
    ? await api("PUT", `/events/${eventId}`, event)
    : await api("POST", "/events", event);
  if (!result) return;

  alert("Evento salvo com sucesso!");
  window.location.href = "index.html";
}

async function start() {
  await loadCategories("category");

  if (eventId) {
    loadEvent();
  }
}

document.getElementById("event-form").addEventListener("submit", saveEvent);
start();
