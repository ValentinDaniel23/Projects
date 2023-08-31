#include <ncurses.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define Ox TABLE[i][j].x
#define Oy TABLE[i][j].y

const int R = 6, // rows of boxes
          C = 5, // columns of boxes
          Block_Height = 3,
          Block_Width = 5,
          Keyboard = 28, // numbers of keys in keyboard
          Wds = 27; // numbers of words in dictionary

typedef struct { // structure for defining a box or a key
    int x, y; // coordinates
    char c, s[10]; // a box has a char and a key can have char or char[]
    WINDOW *block; // entity
} Pair;

void Get_word(char *s) { // list of words in dictionary
    char Words[][6] = {"arici", "atent", "baiat", "ceata", "debut", "peste", "fixat",
                       "hamac", "harta", "jalon", "jucam", "lacat", "magie", "nufar",
                       "oaste", "perus", "rigle", "roman", "sanie", "scris", "sonda",
                       "texte", "tipar", "titan", "zebra", "vapor", "vatra"};

    srand(time(NULL)); 
    int random_number = rand() % Wds; // choose a random number "% Wds" to fit in dictionary
    strcpy(s, Words[random_number]);
}

void Initialize(int *row, int *col) {
    initscr(); // initialises all implementation
    start_color(); // initialises using of colors
    getmaxyx(stdscr, *row, *col); // get the size of the given window

    init_pair(1, COLOR_RED, COLOR_BLACK); init_pair(2, COLOR_GREEN, COLOR_BLACK);
    init_pair(3, COLOR_YELLOW, COLOR_BLACK); init_pair(4, COLOR_BLUE, COLOR_BLACK);
    init_pair(5, COLOR_MAGENTA, COLOR_BLACK); init_pair(6, COLOR_CYAN, COLOR_BLACK);
    init_pair(7, COLOR_BLACK, COLOR_WHITE); init_pair(8, COLOR_WHITE, COLOR_BLACK);
    init_pair(9, COLOR_BLACK, COLOR_YELLOW); init_pair(10, COLOR_BLACK, COLOR_GREEN);
    init_pair(11, COLOR_BLACK, COLOR_RED); init_pair(12, COLOR_BLACK, COLOR_MAGENTA);
    // color combinations used later
}

void Logo(int row, int col) {
    int i, j;
    char s[] = {"WORDLE"}; // logo name
    for (i = 0; i < 6; i++) {
        attron(COLOR_PAIR(i + 1));
        mvaddch(row / 10 - 2, (col - 6) / 2 + i, s[i]); // each letter in the logo with different colors
        attroff(COLOR_PAIR(i + 1));
    }
    refresh();
}

void Create_Keyboard(int row, int col, Pair TABLE[6][5], Pair *Keys) {
    int i = 5, j = 4, z;

    for (z = 0; z < 10; z++) { // first row of keys
        Keys[z].x = Ox + 4;
        Keys[z].y = col / 2 + 1 + (z - 5) * 6;
        Keys[z].block = newwin(Block_Height, Block_Width, Keys[z].x, Keys[z].y);
        box(Keys[z].block, 0, 0);
        wbkgd(Keys[z].block, COLOR_PAIR(7)); 
        wrefresh(Keys[z].block);
    }

    for (z = 0; z < 9; z++) { // second row of keys
        Keys[10 + z].x = Ox + 4 + Block_Height;
        Keys[10 + z].y = col / 2 + Block_Width - 1 + (z - 5) * 6;
        Keys[10 + z].block = newwin(Block_Height, Block_Width, Keys[10 + z].x, Keys[10 + z].y);
        box(Keys[10 + z].block, 0, 0);
        wbkgd(Keys[10 + z].block, COLOR_PAIR(7));
        wrefresh(Keys[10 + z].block);
    }

    for (z = 0; z < 8; z++) { // third row of keys
        Keys[19 + z].x = Ox + 4 + Block_Height * 2;
        Keys[19 + z].y = col / 2 + 1 + (z - 5) * 6;
        Keys[19 + z].block = newwin(Block_Height, Block_Width, Keys[19 + z].x, Keys[19 + z].y);
        box(Keys[19 + z].block, 0, 0);
        wbkgd(Keys[19 + z].block, COLOR_PAIR(7));
        wrefresh(Keys[19 + z].block);
    }

    Keys[27].x = Ox + 4 + Block_Height * 2;
    Keys[27].y = col / 2 + 1 + 3 * 6;
    Keys[27].block = newwin(Block_Height, Block_Width * 2 + 1, Keys[27].x, Keys[27].y);
    box(Keys[27].block, 0, 0);
    wbkgd(Keys[27].block, COLOR_PAIR(7));
    wrefresh(Keys[27].block);

    Keys[0].c = 'q'; Keys[7].c = 'i'; Keys[14].c = 'g'; Keys[21].c = 'x';
    Keys[1].c = 'w'; Keys[8].c = 'o'; Keys[15].c = 'h'; Keys[22].c = 'c';
    Keys[2].c = 'e'; Keys[9].c = 'p'; Keys[16].c = 'j'; Keys[23].c = 'v';
    Keys[3].c = 'r'; Keys[10].c = 'a'; Keys[17].c = 'k'; Keys[24].c = 'b';
    Keys[4].c = 't'; Keys[11].c = 's'; Keys[18].c = 'l'; Keys[25].c = 'n';
    Keys[5].c = 'y'; Keys[12].c = 'd'; Keys[19].c = '\n'; Keys[26].c = 'm';
    Keys[6].c = 'u'; Keys[13].c = 'f'; Keys[20].c = 'z'; Keys[27].c = '\a';
    // each key with its char
    
    strcpy(Keys[19].s, "ENT"); 
    strcpy(Keys[27].s, "backslash");

    attron(COLOR_PAIR(7));
    for (z = 0; z < Keyboard; z++) {
        if (z != 19 && z != 27) {
            mvaddch(Keys[z].x + 1, Keys[z].y + 2, Keys[z].c);
            refresh();
        }
    }

    mvaddstr(Keys[19].x + 1, Keys[19].y + 1, Keys[19].s);
    refresh();

    mvaddstr(Keys[27].x + 1, Keys[27].y + 1, Keys[27].s);
    refresh();

    attroff(COLOR_PAIR(7));
}

void Clear_Game(int row, int col, Pair TABLE[R][C], Pair *Keys) { // function to reset a game
    int i, j;
    for (i = 0; i < col; i++) {
        mvaddch(Keys[Keyboard - 1].x + 6, i, ' '); // print char ' ' to clear 
    }
    refresh();

    for (i = 0; i < col; i++) {
        mvaddch(Keys[Keyboard - 1].x + 8, i, ' '); // print char ' ' to clear
    }
    refresh();

    for (i = 0; i < R; i++) {
        for (j = 0; j < C; j++) {
            mvaddch(TABLE[i][j].x + 1, TABLE[i][j].y + 1, ' ');
            mvaddch(TABLE[i][j].x + 1, TABLE[i][j].y + 2, ' ');
            mvaddch(TABLE[i][j].x + 1, TABLE[i][j].y + 3, ' ');
            TABLE[i][j].c = ' ';
            /// char ' ' to clear all
            refresh();
        }
    }
}

void Score(int row, int col, int *score) { // function to show the score in the corner
    int i;
    refresh();
    mvaddstr(1, 1, "Your score is : "); 
    int nr = 0, v[10] = {0}, cpy = *score;
    while (cpy) {
        v[nr++] = cpy % 10;
        cpy /= 10;
    }
    // get each digit
    if (nr == 0) nr = 1;
    for (i = nr - 1; i >= 0; i--) {
        mvaddch(1, 17 + (nr - 1) - i, '0' + v[i]);
        refresh();
    }
    // print each digit
}

void Menu(int row, int col) { // function to show menu in the corner -> ":"
    init_pair(13, COLOR_BLACK, COLOR_BLUE);
    attron(COLOR_PAIR(13));
    mvaddch(row - 4, col - 4, ':');
    attroff(COLOR_PAIR(13));
}

void Close_Menu(int row, int col) { // function to close the menu
    int i;
    for (i = 0; i < col; i++) {
        mvaddch(row - 4, i, ' ');
        mvaddch(row - 3, i, ' ');
        // char ' ' to clear
    }
    refresh();
}

char Open_Menu(int row, int col) { // function to open the menu
    char s[30], c;
    attron(COLOR_PAIR(13));
    strcpy(s, " Press 1 to leave the game   ");
    mvaddstr(row - 4, col - 4 - strlen(s), s);
    strcpy(s, " Press 2 to start a new game ");
    mvaddstr(row - 3, col - 4 - strlen(s), s);
    attroff(COLOR_PAIR(13));

    while (c = getch()) { // read until it get '1' or '2'
        if (c == '1') {
            break;
        } else if (c == '2') {
            break;
        }
    }

    Close_Menu(row, col);
    return c; // return order 1 or 2
}

void Gameplay(int row, int col, Pair TABLE[R][C], Pair *Keys, int *score) {
    int i = 0, j, z, k;
    char c, word[6];
    Get_word(word); // get the random hidden word for the game

    keypad(stdscr, TRUE); // activate any key
    echo(); // can print
    curs_set(1); // cursor display is enabled

    Score(row, col, score); // display score
    Menu(row, col); // display menu
    refresh();

    while (i < R) { // each row
        j = 0;
        while (j < C) { // each column
            move(Ox + 1, Oy + 2); // move cursor
            c = getch();

            if (c == ':') { // use menu
                noecho();
                curs_set(0);
                c = Open_Menu(row, col);
                if (c == '2') {
                    Clear_Game(row, col, TABLE, Keys);
                    Gameplay(row, col, TABLE, Keys, score);
                }

                return;
            }

            for (z = 0; z < col; z++) { // delete any previous text " Form a 5-letter word! "
                mvaddch(Keys[Keyboard - 1].x + 6, z, ' ');
            }
            refresh();

            bool ok = 0;
            for (z = 0; z < Keyboard; z++) { // c is in keyboard
                if (c == Keys[z].c) ok = 1;
            }

            if (ok) {
                if (c == '\a') { // c is backslash
                    if (j > 0) j--;
                    TABLE[i][j].c = ' ';
                } else if (c != '\n') {  // c is not newline
                    TABLE[i][j].c = c;
                    j++;
                } else { // c is newline
                    char s[] = " Form a 5-letter word! ";
                    attron(COLOR_PAIR(9));
                    mvaddstr(Keys[Keyboard - 1].x + 6, (col - strlen(s)) / 2, s);
                    attroff(COLOR_PAIR(9));
                }
            }

            for (z = 0; z < C; z++) { // displays the characters in the current line
                mvaddch(TABLE[i][z].x + 1, TABLE[i][z].y + 2, TABLE[i][z].c);
                refresh();
            }

            if (j == C) { // the row is completed
                noecho(); // can't print
                curs_set(0); // cursor display is disabled
                while (c = getch()) {
                    if (c == ':') { // use menu
                        noecho();
                        curs_set(0);
                        c = Open_Menu(row, col);
                        if (c == '2') {
                            Clear_Game(row, col, TABLE, Keys);
                            Gameplay(row, col, TABLE, Keys, score);
                        }

                        return;
                    }
                    if (c == '\n') { // go to next row
                        int letters_found = 0;
                        for (z = 0; z < C; z++) {
                            int color = 8; // basic color
                            for (k = 0; k < C; k++) {
                                if (TABLE[i][z].c == word[k]) color = 9; // yellow color
                            }
                            if (TABLE[i][z].c == word[z]) {
                                color = 10; // red color
                                letters_found++;
                            }

                            attron(COLOR_PAIR(color));
                            mvaddch(TABLE[i][z].x + 1, TABLE[i][z].y + 1, ' ');
                            mvaddch(TABLE[i][z].x + 1, TABLE[i][z].y + 2, TABLE[i][z].c);
                            mvaddch(TABLE[i][z].x + 1, TABLE[i][z].y + 3, ' ');
                            attroff(COLOR_PAIR(color));
                            /// color the line after newline

                            refresh();
                        }

                        if (letters_found == 5) { /// game over, it is win
                            char s[] = " !!!Congratulations!!! ";
                            attron(COLOR_PAIR(10));
                            mvaddstr(Keys[Keyboard - 1].x + 6, (col - strlen(s)) / 2, s);
                            attroff(COLOR_PAIR(10));

                            strcpy(s, " Press y to play again ");
                            attron(COLOR_PAIR(12));
                            mvaddstr(Keys[Keyboard - 1].x + 8, 1, s);
                            attroff(COLOR_PAIR(12));

                            noecho();
                            curs_set(0);
                            (*score)++;
                            while (c = getch()) {
                                if (c == 'y') {
                                    break;
                                } else if (c == ':') {
                                    c = Open_Menu(row, col);
                                    break;
                                }
                            }
                            /// read until you type get 'y' or ':'

                            if (c == 'y' || c == '2') {
                                Clear_Game(row, col, TABLE, Keys);
                                Gameplay(row, col, TABLE, Keys, score);
                            }
                            /// 'y' and '2' (from Open_Menu) means new game
                            /// else it is '1' which means to leave the game

                            return;
                        }

                        break;
                    } else if (c == '\a') { /// after 5 characters, press backslash instead of enter
                        j--;
                        TABLE[i][j].c = ' ';
                        for (z = 0; z < C; z++) {
                            mvaddch(TABLE[i][z].x + 1, TABLE[i][z].y + 2, TABLE[i][z].c);
                            refresh();
                        }
                        break;
                    }
                }
                curs_set(1); 
                echo();
            }
        }
        i++;
    }
    noecho();
    curs_set(0);

    char s[30] = " The hidden word was: ";
    strcat(s, word);
    strcat(s, " ");
    attron(COLOR_PAIR(11));
    mvaddstr(Keys[Keyboard - 1].x + 6, (col - strlen(s)) / 2, s);
    attroff(COLOR_PAIR(11));
    /// show the word after a lost game

    strcpy(s, " Press y to play again ");
    attron(COLOR_PAIR(12));
    mvaddstr(Keys[Keyboard - 1].x + 8, 1, s);
    attroff(COLOR_PAIR(12));

    while (c = getch()) {
        if (c == 'y') {
            break;
        } else if (c == ':') {
            c = Open_Menu(row, col);
            break;
        }
    }

    if (c == 'y' || c == '2') {
        Clear_Game(row, col, TABLE, Keys);
        Gameplay(row, col, TABLE, Keys, score);
    }

    return;
}

int main() {
    int row, col, i, j, pos, score = 0;
    Pair TABLE[R][C];

    Initialize(&row, &col);
    Logo(row, col);

    for (i = 0; i < 6; i++) {
        for (j = 0; j < 5; j++) {
            TABLE[i][j].x = row / 10 + 1 + i * 4 - 2;
            TABLE[i][j].y = col / 2 - 2 + (j - 2) * 8;
            TABLE[i][j].block = newwin(Block_Height, Block_Width, Ox, Oy);
            TABLE[i][j].c = ' ';
            box(TABLE[i][j].block, 0, 0);
            wrefresh(TABLE[i][j].block);
        }
    }
    /// initialize boxes for the game

    Pair Keys[Keyboard];
    Create_Keyboard(row, col, TABLE, Keys);
    Gameplay(row, col, TABLE, Keys, &score); /// start game

    clear(); /// clear window
    char s[] = "Your score is : ";

    for (i = 0; i < strlen(s); i++) {
        attron(COLOR_PAIR(i % 6 + 1));
        mvaddch(row / 2, (col - strlen(s)) / 2 + i, s[i]);
        attroff(COLOR_PAIR(i % 6 + 1));
    }
    int nr = 0, v[10] = {0}, cpy = score;
    while (cpy) {
        v[nr++] = cpy % 10;
        cpy /= 10;
    }
    if (nr == 0) nr = 1;
    score = cpy;
    for (i = nr - 1; i >= 0; i--) {
        mvaddch(row / 2 + 1, col / 2, '0' + v[i]);
        refresh();
    }
    /// display game score before leaving the game

    refresh();
    getch();
    refresh();
    endwin();
    return 0;
}