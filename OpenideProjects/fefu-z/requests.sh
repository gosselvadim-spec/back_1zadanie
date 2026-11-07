#!/bin/bash
BASE="http://localhost:8000"

echo "1. создать повара medium:"
curl -s -X POST $BASE/chefs -H "Content-Type: application/json" -d '{"name":"Иван", "type":"medium"}'
echo -e "\n"

echo "2. Создать повара ulti:"
curl -s -X POST $BASE/chefs -H "Content-Type: application/json" -d '{"name":"Петр", "type":"ulti"}'
echo -e "\n"

echo "3. Ошибка: создать повара с левым типом (должен быть 400):"
curl -s -X POST $BASE/chefs -H "Content-Type: application/json" -d '{"name":"Вася", "type":"boss"}'
echo -e "\n"

echo "4. Создать блюдо для Ивана:"
curl -s -X POST $BASE/dishes -H "Content-Type: application/json" -d '{"name":"Борщ", "chefId":"ID_ПОВАРА"}'
echo -e "\n"

echo "5. Получить все блюда (должен быть список):"
curl -s -X GET $BASE/dishes
echo -e "\n"

echo "6. Получить только блюда в статусе CREATED (фильтрация):"
curl -s -X GET "$BASE/dishes?status=CREATED"
echo -e "\n"

echo "7. Начать готовить блюдо (статус станет COOKING):"
curl -s -X POST $BASE/dishes/ID_БЛЮДА/cook
echo -e "\n"

echo "8. Ошибка: приготовить это же блюдо еще раз (должен быть 400):"
curl -s -X POST $BASE/dishes/ID_БЛЮДА/cook
echo -e "\n"

echo "9. Изменить название блюда (PUT):"
curl -s -X PUT $BASE/dishes/ID_БЛЮДА -H "Content-Type: application/json" -d '{"name":"Суп Борщ"}'
echo -e "\n"

echo "10. Удалить блюдо (DELETE) - но оно уже готовится, значит будет ошибка 400:"
curl -s -X DELETE $BASE/dishes/ID_БЛЮДА
echo -e "\n"