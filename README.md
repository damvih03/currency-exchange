# Currency exchange

Учебный проект "[Обмен валют](https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/)"
из [курса](https://zhukovsd.github.io/java-backend-learning-course/) Сергея Жукова.

### Используемые технологии и инструменты:

* Java Core
* Java Servlets
* JDBC
* SQLite
* Lombok
* MapStruct
* HikariCP
* Jackson
* Maven

### Запросы:

* #### GET `/currencies`

Получить список валют. Пример ответа:

```json
[
  {
    "id": 1,
    "code": "USD",
    "name": "United States Dollar",
    "sign": "$"
  },
  {
    "id": 2,
    "code": "EUR",
    "name": "Euro",
    "sign": "€"
  },
  "..."
]
```

<br>

* #### GET `/exchangeRates`

Получить список обменных курсов. Пример ответа:

```json
[
  {
    "id": 1,
    "baseCurrency": {
      "id": 1,
      "code": "USD",
      "name": "United States Dollar",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 2,
      "code": "EUR",
      "name": "Euro",
      "sign": "€"
    },
    "rate": 0.935
  },
  {
    "id": 2,
    "baseCurrency": {
      "id": 1,
      "code": "USD",
      "name": "United States Dollar",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "code": "RUB",
      "name": "Russian Ruble",
      "sign": "₽"
    },
    "rate": 88.25
  },
  "..."
]
```

<br>

* #### GET `/currency/RUB`

Получить конкретную валюту. Код валюты передается в адресе запроса. Пример ответа:

```json
{
  "id": 3,
  "code": "RUB",
  "name": "Russian Ruble",
  "sign": "₽"
}
```

<br>

* #### GET `/exchangeRate/USDRUB`

Получить конкретный обменный курс. Коды валют передаются в адресе запроса. Пример ответа:

```json
{
  "id": 2,
  "baseCurrency": {
    "id": 1,
    "code": "USD",
    "name": "United States Dollar",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 3,
    "code": "RUB",
    "name": "Russian Ruble",
    "sign": "₽"
  },
  "rate": 88.25
}
```

<br>

* #### GET `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`

Конвертировать сумму из одной валюты в другую. Коды валют и сумма передаются в адресе запроса как параметры. Пример
ответа:

```json
{
  "baseCurrency": {
    "id": 1,
    "code": "USD",
    "name": "United States Dollar",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 3,
    "code": "RUB",
    "name": "Russian Ruble",
    "sign": "₽"
  },
  "rate": 88.25,
  "amount": 2,
  "convertedAmount": 176.50
}
```

<br>

* #### POST `/currencies`

Добавить новую валюту в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Поля формы -
name, code, sign. Пример ответа:

```json
{
  "id": 4,
  "code": "VND",
  "name": "Vietnamese đồng",
  "sign": "₫"
}
```

<br>

* #### POST `/exchangeRates`

Добавить новый обменный курс в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Поля
формы - baseCurrencyCode, targetCurrencyCode, rate. Пример ответа:

```json
{
  "id": 3,
  "baseCurrency": {
    "id": 1,
    "code": "USD",
    "name": "United States Dollar",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 4,
    "code": "VND",
    "name": "Vietnamese đồng",
    "sign": "₫"
  },
  "rate": 25420.0
}
```

<br>

* #### PATCH `/exchangeRate/USDRUB`

Обновить существующий в базе обменный курс. Валютная пара задается идущими подряд кодами валют в адресе запроса. Данные
передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Единственное поле формы - rate. Пример ответа:

```json
{
  "id": 2,
  "baseCurrency": {
    "id": 1,
    "code": "USD",
    "name": "United States Dollar",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 3,
    "code": "RUB",
    "name": "Russian Ruble",
    "sign": "₽"
  },
  "rate": 87.43
}
```