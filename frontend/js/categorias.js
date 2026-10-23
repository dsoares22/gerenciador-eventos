async function listCategories() {
  const categories = await api("GET", "/categories");
  if (!categories) return;

  const table = document.getElementById("categories");
  table.innerHTML = "";

  for (const category of categories) {
    table.innerHTML += `
            <tr>
                <td>${category.name}</td>
                <td>${category.description || ""}</td>
                <td>
                    <button onclick="editCategory(${category.id})">Editar</button>
                    <button onclick="deleteCategory(${category.id})">Excluir</button>
                </td>
            </tr>
        `;
  }
}

async function saveCategory(event) {
  event.preventDefault();

  const id = document.getElementById("id").value;
  const category = {
    name: document.getElementById("name").value,
    description: document.getElementById("description").value,
  };

  const result = id
    ? await api("PUT", `/categories/${id}`, category)
    : await api("POST", "/categories", category);
  if (!result) return;

  alert("Categoria salva com sucesso!");
  document.getElementById("form").reset();
  document.getElementById("id").value = "";
  listCategories();
}

async function editCategory(id) {
  const category = await api("GET", `/categories/${id}`);
  if (!category) return;

  document.getElementById("id").value = category.id;
  document.getElementById("name").value = category.name;
  document.getElementById("description").value = category.description || "";
}

async function deleteCategory(id) {
  if (!confirm("Tem certeza que deseja excluir esta categoria?")) return;

  if (await api("DELETE", `/categories/${id}`)) {
    listCategories();
  }
}

document.getElementById("form").addEventListener("submit", saveCategory);
listCategories();
