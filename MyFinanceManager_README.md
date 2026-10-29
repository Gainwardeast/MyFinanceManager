# MyFinanceManager

Простое и наглядное приложение для управления личными финансами (финансовый планировщик). Позволяет вести учёт доходов и расходов по месяцам, контролировать постоянные траты и видеть структуру расходов на диаграмме.

> ⚠️ Проект в ранней стадии разработки.

## ✨ Возможности

- 📅 **Учёт по месяцам** — переключение между месяцами, выбор произвольного месяца и года
- 💰 **Доходы** — добавление и просмотр поступлений за выбранный месяц
- 💸 **Текущие расходы** — быстрое добавление трат через диалог
- 📌 **Постоянные расходы** — отдельный раздел для регулярных платежей (аренда, подписки и т.п.)
- 📊 **Диаграмма расходов** — круговая диаграмма по статьям трат
- 🗄 **Локальное хранение** — все данные хранятся на устройстве (Room), без интернета и серверов

## 🛠 Технологии

| Категория | Технологии |
|---|---|
| Язык | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Архитектура | MVVM (ViewModel + StateFlow), Repository |
| База данных | Room 2.6.1 (KSP) |
| Асинхронность | Kotlin Coroutines / Flow |
| Сборка | Gradle (Kotlin DSL), Version Catalog |

## 🏗 Структура проекта

```
app/src/main/java/com/example/myfinancemanager/
├── components/          # Compose-компоненты экрана
│   ├── AddExpenseDialog.kt        # диалог добавления расхода
│   ├── IncomeSection.kt           # блок доходов
│   ├── CurrentExpensesSection.kt  # блок текущих расходов
│   ├── FixedExpensesSection.kt    # блок постоянных расходов
│   ├── PieChart.kt                # круговая диаграмма
│   └── MonthSelector.kt           # выбор месяца/года
├── data/                # слой хранения (Room)
│   ├── AppDatabase.kt, ExpenseDao.kt, IncomeDao.kt
│   └── ExpenseEntity.kt, IncomeEntity.kt, ExpenseMapper.kt
├── repository/          # репозитории (FinanceRepository, Expense/IncomeRepository)
├── model/               # модели данных и записи диаграммы
├── ui/                  # тема приложения
├── MainViewModel.kt     # состояние и логика главного экрана
└── MainScreen.kt        # Compose-экран
```

## 🚀 Сборка и запуск

**Требования:**
- Android Studio (Koala/новее рекомендуются)
- JDK 17
- Минимальная версия Android: **7.0 (API 24)**, целевая: **14 (API 34)**

```bash
git clone https://github.com/Gainwardeast/MyFinanceManager.git
cd MyFinanceManager
./gradlew installDebug   # или открыть проект в Android Studio и нажать Run
```

## 🗺 Планы

- [ ] Редактирование и удаление записей
- [ ] Категории расходов с настройкой цветов
- [ ] Экспорт данных (CSV)
- [ ] Тёмная тема
- [ ] Локализация (английский)

## 🤝 Участие

Идеи и багрепорты приветствуются — открывайте [Issues](https://github.com/Gainwardeast/MyFinanceManager/issues).

## 📄 Лицензия

Пока не определена. (Добавьте LICENSE, если хотите опубликовать проект как open source.)
