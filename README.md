# TanksProject
Тема проекта
Разработка клиент-серверного приложения-игры с помощью Java Socket API и JavaFX.
Цели и задачи проекта
Применить знания, полученные в ходе прохождения курса, в разработке
многопользовательского приложения на Java.
Форма работы
Индивидуальная
Набор технологий
Java Core (Collection API, IO/NIO, Sockets), JavaFX
Критерии проекта
Требования к функциональности
● Приложение должны содержать элементы для ввода имени пользователя
● Приложение должно содержать окно-игровое поле, на котором присутствуют
более 2-х объектов-противников
● Игровой процесс приложения должен позволять осуществлять взаимодействие
между игроками
● Приложение должно позволять осуществлять перемещение объектов по
игровому полю с помощью клавиатуры (не выходя за границы игровой области).
● Результат взаимодействия игроков (очки, статистика и т.д.) должны сохранятся в
СУБД.
Требования к архитектуре
● Приложение должно содержать серверную часть со следующими элементами:
○ Класс SocketServer (данный класс предоставляет функциональность
для подключения)
○ Класс SocketClient (данный класс содержит логику взаимодействия с
socket-клиентами)
○ Пакет models (содержит модели предметной области)
○ Пакет services (содержит классы для реализации бизнес-логики)
○ Пакет repositories (содержит классы для взаимодействия с СУБД)
Дополнительные требования
Обеспечить хранение актуальной информации в базе данных на
момент окончания игры.
Игра окончена, когда один из клиентов посылает сообщение
"stop".
Клиент посылает "stop", когда у него 0 hp.
В базе должна хранится актуальная информация по:
Игроку:
- Последнему IP игрока
- Количество очков игрока на все игры
- Количество поражений
- Количество побед
------
Игре:
- Сколько попаданий у первого игрока
- Сколько попаданий у второго игрока
- Сколько длилась игра (по желанию).
Критерии успешной защиты проекта
Обучающийся:
● самостоятельно реализовал функционал по сохранению актуальной
информации в базе данных
● самостоятельно реализовал функционал по завершению игры
● умеет самостоятельно реализовывать приложение по заданию согласно
требуемой архитектуре
● дает правильные определения использованным механикам языка
программирования
● отвечает на вопросы, касающиеся реализованного проекта
● отвечает на вопросы, касающиеся программы курса
● демонстрирует полностью работоспособное приложение, отвечающие
требованиям гибкого программного обеспечения
