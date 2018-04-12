#include <stdlib.h>

static const char *MORSE_LOOKUP_TABLE[] = {
//   A     B       C       D      E    F       G      H       I     J
    ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
//   K      L       M     N     O      P       Q       R      S      T
    "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-",
//   U      V       W      X       Y       Z
    "..-", "...-", ".--", "-..-", "-.--", "--.."
};

static const char MESSAGE[] = "SOS";
static size_t MESSAGE_LENGTH = sizeof(MESSAGE) - 1;

static unsigned int PERIOD_FOR_DOT  = 300,
                    PERIOD_FOR_DASH = 1200;

static unsigned int DELAY_BETWEEN_LETTERS  = 400,
                    DELAY_BETWEEN_MESSAGES = 3000;

static const int LED_PIN = LED_BUILTIN;

static const int BUTTON_PIN = 13;
unsigned long BUTTON_DEBOUNCE_DELAY = 1000;

unsigned long last_button_debounce_time = 0;

static volatile bool is_working = false;

static volatile size_t letter_index = 0;

char to_uppercase(char letter)
{
    if (letter >= 'a' && letter <= 'z') {
        letter -= 'a' - 'A';
    }

    return letter;
}

bool is_letter(char character)
{
    return character >= 'A' && character <= 'z';
}

void blink_and_buzz(unsigned int periodInMilliseconds)
{
    unsigned int pauseInMilliseconds = periodInMilliseconds / 2;

    digitalWrite(LED_PIN, HIGH);
    delay(pauseInMilliseconds);
    digitalWrite(LED_PIN, LOW);
    delay(pauseInMilliseconds);
}

void ping_morse(const char *morse_sequence)
{
    while (is_working && *morse_sequence) {
        if (*morse_sequence == '.') {
            blink_and_buzz(PERIOD_FOR_DOT);
        } else {
            blink_and_buzz(PERIOD_FOR_DASH);
        }

        ++morse_sequence;
    }
}

void change_state()
{
    if (digitalRead(BUTTON_PIN) == LOW) {
        if ((millis() - last_button_debounce_time) > BUTTON_DEBOUNCE_DELAY) {
            last_button_debounce_time = millis();
            is_working = !is_working;
            if (!is_working) {
                reset();
            }
        }
    }
}

void reset()
{
    digitalWrite(LED_PIN, LOW);
}

void setup()
{
    pinMode(LED_PIN, OUTPUT);
    pinMode(BUTTON_PIN, INPUT_PULLUP);

    attachInterrupt(digitalPinToInterrupt(BUTTON_PIN), change_state, CHANGE);
}

void loop()
{
    char character, letter;
    const char *morse_sequence_for_letter;

    if (!is_working) {
        return;
    }

    while (!is_letter(character = MESSAGE[letter_index])) {
        letter_index = (letter_index + 1) % MESSAGE_LENGTH;
    }

    letter = to_uppercase(character);
    morse_sequence_for_letter = MORSE_LOOKUP_TABLE[letter - 'A'];

    ping_morse(morse_sequence_for_letter);

    ++letter_index;
    if (letter_index >= MESSAGE_LENGTH) {
        letter_index = letter_index % MESSAGE_LENGTH;
        delay(DELAY_BETWEEN_MESSAGES);
    } else {
        delay(DELAY_BETWEEN_LETTERS);
    }
}

