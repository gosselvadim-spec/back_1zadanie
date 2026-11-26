#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== 1. Создание повара (medium) ==="
CHEF_JSON=$(curl -s -X POST $BASE_URL/chefs -H "Content-Type: application/json" -d '{"name":"Иван", "type":"medium"}')
echo $CHEF_JSON
CHEF_ID=$(echo $CHEF_JSON | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "Создан повар с ID: $CHEF_ID\n"

echo "=== 2. Ошибка: Создание повара с неправильным типом (ожидаем 400) ==="
curl -s -X POST $BASE_URL/chefs -H "Content-Type: application/json" -d '{"name":"Вася", "type":"boss"}'
echo -e "\n"

echo "=== 3. Создание блюда для Ивана (используем ID повара) ==="
DISH_JSON=$(curl -s -X POST $BASE_URL/dishes -H "Content-Type: application/json" -d "{\"name\":\"Борщ\", \"chefId\":\"$CHEF_ID\"}")
echo $DISH_JSON
DISH_ID=$(echo $DISH_JSON | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "Создано блюдо с ID: $DISH_ID\n"

echo "=== 4. Приготовление Борща ==="
curl -s -X POST $BASE_URL/dishes/$DISH_ID/cook
echo -e "\n"

echo "=== 5. Ошибка: Приготовить Борщ еще раз (ожидаем 409) ==="
curl -s -X POST $BASE_URL/dishes/$DISH_ID/cook
echo -e "\n"

echo "=== 6. Изменение названия блюда (PUT) ==="
curl -s -X PUT $BASE_URL/dishes/$DISH_ID -H "Content-Type: application/json" -d '{"name":"Суп Борщ"}'
echo -e "\n"

echo "=== 7. Удаление блюда (DELETE) ==="
DEL_JSON=$(curl -s -X POST $BASE_URL/dishes -H "Content-Type: application/json" -d "{\"name\":\"Удаляемое\", \"chefId\":\"$CHEF_ID\"}")
DEL_ID=$(echo $DEL_JSON | grep -o '"id":"[^"]*' | cut -d'"' -f4)
curl -s -X DELETE $BASE_URL/dishes/$DEL_ID
echo -e "\n"

echo "=== 8. Ошибка: Удаление несуществующего блюда (ожидаем 400) ==="
curl -s -X DELETE $BASE_URL/dishes/unknown_id
echo -e "\n"

echo "=== 9. Получение повара по ID ==="
curl -s -X GET $BASE_URL/chefs/$CHEF_ID
echo -e "\n"

echo "=== 10. Удаление повара ==="
curl -s -X DELETE $BASE_URL/chefs/$CHEF_ID
echo -e "\n"