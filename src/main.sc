require: scripts/functions.js
theme: /
    
    state: Start
        q!: $regex</start>
        script:
            $session = {};
            $client = {};
            $temp = {};
            $response = {};
            $session.attempts = 0;
            $session.won = false;
            $session.secretNumber = generateNumber();
        a: Привет! Давай сыграем в "Быки и коровы". Я загадал 4-значное число {{ $session.secretNumber }}. Попробуй угадать его. Введи своё первое предположение!

    state: Guess
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
            }
    
            $temp.bulls = bulls;
            $temp.cows = cows;
    
        if: $session.won
            a: Поздравляю! Ты угадал число {{ $session.secretNumber }} за {{ $session.attempts }} попыток. Хочешь сыграть ещё раз?
            go!: Start
    
        else:
            a: Быки: {{ $temp.bulls }}, Коровы: {{ $temp.cows }}. Попробуй ещё раз!
            go!: Guess
    
    state: InvalidInput
        q!: *
        a: Пожалуйста, введи 4-значное число. Попробуй ещё раз!
        go!: Guess
    
    state: Fallback || noContext = true
        event!: noMatch
        a: Извините, я вас не понимаю. Попробуйте снова начать игру, введя команду "start".
        go!: Start
        
