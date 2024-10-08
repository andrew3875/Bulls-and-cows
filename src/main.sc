require: scripts/functions.js

theme: /
    
    state: Start
        q!: ($regex</start>|start)
        script:
            $session = {};
            $temp = {};
            $session.attempts = 0;
            $session.won = false;
            $session.secretNumber = generateNumber();
        a: Привет! Давай сыграем в "Быки и коровы". Я загадал 4-значное число. Попробуй угадать его. Введи своё первое предположение!

    state: Guess
        q: $regex<^\d{4}$>
        script:
            if (!$session.won) {
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
            }
    
        if: $session.won
            a: Поздравляю! Ты угадал число {{ $session.secretNumber }} за {{ $session.attempts }} попыток. Введи start для новой игры.
        else:
            a: Быки: {{ $temp.bulls }}, коровы: {{ $temp.cows }}. Попробуй ещё раз!

    state: InvalidInput
        q!: * 
        if: $session.won
            a: Введи start для новой игры.
        else:
            a: Пожалуйста, введи 4-значное число.

