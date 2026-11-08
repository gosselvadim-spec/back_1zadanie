#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== 1. Создание повара (medium) ==="
curl -s -X POST $BASE_URL/chefs -H "Content-Type: application/json" -d '{"name":"Иван", "type":"medium"}'
echo -e "\n"

echo "=== 2. Создание повара (ulti) ==="
curl -s -X POST $BASE_URL/chefs -H "Content-Type: application/json" -d '{"name":"Петр", "type":"ulti"}'
echo -e "\n"

echo "=== 3. Ошибка: Создание повара с неправильным типом (ожидаем 400) ==="
curl -s -X POST $BASE_URL/chefs -H "Content-Type: application/json" -d '{"name":"Вася", "type":"boss"}'
echo -e "\n"

echo "=== 4. Создание блюда для Ивана ==="
# ВАЖНО: Замени ID_ИВАНА на реальный ID, который вернул сервер в шаге 1
curl -s -X POST $BASE_URL/dishes -H "Content-Type: application/json" -d '{"name":"Борщ", "chefId":"ID_ИВАНА"}'
echo -e "\n"

echo "=== 5. Ошибка: Создание блюда для несуществующего повара (ожидаем 400) ==="
curl -s -X POST $BASE_URL/dishes -H "Content-Type: application/json" -d '{"name":"Салат", "chefId":"fake_id"}'
echo -e "\n"

echo "=== 6. Получение списка всех блюд ==="
curl -s -X GET $BASE_URL/dishes
echo -e "\n"

echo "=== 7. Фильтрация блюд по статусу CREATED ==="
curl -s -X GET "$BASE_URL/dishes?status=CREATED"
echo -e "\n"

echo "=== 8. Приготовление Борща ==="
# ВАЖНО: Замени ID_БОРЩА на ID из шага 4
curl -s -X POST $BASE_URL/dishes/ID_БОРЩА/cook
echo -e "\n"

echo "=== 9. Ошибка: Приготовить Борщ еще раз (ожидаем 400) ==="
curl -s -X POST $BASE_URL/dishes/ID_БОРЩА/cook
echo -e "\n"

echo "=== 10. Изменение названия блюда (PUT) ==="
curl -s -X PUT $BASE_URL/dishes/ID_БОРЩА -H "Content-Type: application/json" -d '{"name":"Суп Борщ"}'
echo -e "\n"

echo "=== 11. Удаление блюда (DELETE) ==="
# Создадим новое блюдо для удаления
curl -s -X POST $BASE_URL/dishes -H "Content-Type: application/json" -d '{"name":"Удаляемое", "chefId":"ID_ИВАНА"}'
echo -e "\n"
# ВАЖНО: Замени ID_УДАЛЯЕМОГО на его реальный ID
curl -s -X DELETE $BASE_URL/dishes/ID_УДАЛЯЕМОГО
echo -e "\n"

echo "=== 12. Ошибка: Удаление несуществующего блюда (ожидаем 400) ==="
curl -s -X DELETE $BASE_URL/dishes/unknown_id
echo -e "\n"