const API_URL = "http://localhost:8080/api";

async function api(method, path, data) {
  try {
    const response = await fetch(API_URL + path, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: data ? JSON.stringify(data) : undefined,
    });

    if (!response.ok) {
      const error = await response.json();
      alert(error.message);
      return null;
    }

    return response.status === 204 ? true : response.json();
  } catch (error) {
    alert(
      "Não foi possível conectar à API. Verifique se o backend está rodando.",
    );
    return null;
  }
}

async function loadCategories(selectId) {
  const categories = await api("GET", "/categories");
  if (!categories) return;

  const select = document.getElementById(selectId);
  for (const category of categories) {
    select.innerHTML += `<option value="${category.id}">${category.name}</option>`;
  }
}
