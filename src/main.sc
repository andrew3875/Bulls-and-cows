require: scripts/functions.js
theme: /
    
    state: start
        #a: Добро пожаловать в игру "Быки и коровы"! Угадай 4-значное число. Введи "начать" для начала игры.
        q!: (начать|играть|давай играть|сыграем|новая игра)
        script:
            $session.secretNumber = generateNumber();
            $session.attempts = 0;
        a: Игра началась! Я загадал 4-значное число {{$session.secretNumber}}. Попробуй угадать!
        go: guess

    state: guess
        q!: $regex<^\d{4}$>
        script:
            $session.attempts += 1;
            var secret = $session.secretNumber;
            var guess = $parseTree.text;
            var bulls = 0;
            var cows = 0;

            for (var i = 0; i < 4; i++) {
                if (secret[i] === guess[i]) {
                    bulls += 1;
                } else if (secret.indexOf(guess[i]) !== -1) {
                    cows += 1;
                }
            }

            if (bulls === 4) {
                $session.won = true;
            } else {
                $session.won = false;
            }

            $temp.bulls = bulls;
            $temp.cows = cows;
        if: $session.won
            a: Поздравляю! Ты угадал число {{ $session.secretNumber }} за {{ $session.attempts }} попыток. Хотите сыграть еще раз?
            go: start
        else:
            a: Быки: {{ $temp.bulls }}, Коровы: {{ $temp.cows }}. Попробуй еще раз!
            go: guess

    state: fallback
        q!: *
        a: Я тебя не понимаю. Введи 4-значное число или скажи "начать" для новой игры.
        go: guess