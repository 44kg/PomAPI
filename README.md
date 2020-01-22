# PomApi

В аргументы передать порт.

Настройки Hibernate в /resources/db_config.xml

POST /insert_pom сохранение pom.xml в БД.

GET  /dependent_gavs?groupId=value&artifactId=value&version=value получить список зависимых артефактов

GET  /main_gavs?groupId=value&artifactId=value&version=value получить список артефактов, для которых данный является зависимым

GET  /get_pom?groupId=value&artifactId=value&version=value получить pom по артефакту

GET  /get_most_used_gavs?amount=value получить список самых частых зависимостей. amount - величина списка, без параметров = 10

