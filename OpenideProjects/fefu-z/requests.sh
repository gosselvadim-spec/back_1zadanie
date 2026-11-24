#!/bin/bash
BASE="http://localhost:8080"

echo "=== 1. Создание повара ==="
CHEF_JSON=$(curl -s -X POST $BASE/chefs -H "Content-Type: application/json" -d '{"name":"Иван", "type":"medium"}')
echo $CHEF_JSON
CHEF_ID=$(echo $CHEF_JSON | grep -o '"id":"[^"]*' | cut -d'"' -f4)

echo -e "\n=== 2. Создание блюда ==="
DISH_JSON=$(curl -s -X POST $BASE/dishes -H "Content-Type: application/json" -d "{\"name\":\"Борщ\", \"chefId\":\"$CHEF_ID\"}")
echo $DISH_JSON
DISH_ID=$(echo $DISH_JSON | grep -o '"id":"[^"]*' | cut -d'"' -f4)

echo -e "\n=== 3. Приготовление блюда ==="
curl -s -X POST $BASE/dishes/$DISH_ID/cook
echo -e "\n"

echo -e "=== 4. Ошибка: Приготовить еще раз (должен быть 409) ==="
curl -s -X POST $BASE/dishes/$DISH_ID/cook
echo -e "\n"

echo -e "=== 5. Получение списка блюд (проверка н+1) ==="
curl -s -X GET $BASE/dishes