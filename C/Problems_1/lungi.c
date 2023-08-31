#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

const int DIM=1001;

/// schimba semnele unui numar complex
void Change_sings(int n,char *s){
    s[0]='0'+'1'-s[0]; 
    s[(n-1)/2]='0'+'1'-s[(n-1)/2];
} /// 0 devine 1 si viceversa

/// se transmit 2 numere, si se returneaza relatia de ordine dintre primul si al doilea
bool Compare(int n,char *s,char *t){
    int i;
    for(i=1; i<(n-1)/2; i++){
        if(s[i]>t[i]) return 0; /// primul e mai mare
        if(s[i]<t[i]) return 1; /// al doilea e mai mare
    }

    return 0; /// primul e mai mare
}

/// se schimba 2 numere intre ele
void Swap(int n,char *s,char *t){
    int i;
    for(i=0; i<(n-1)/2; i++){
        char c=s[i];
        s[i]=t[i];
        t[i]=c;
    }
}

/// adunarea a 2 numere
void Sum(int n,char *s,char *t){
    int i,tr=0;
    for(i=(n-1)/2-1; i>0; i--){
        tr+=s[i]-'0'+t[i]-'0';
        s[i]='0'+tr%10;
        tr/=10;
    } /// adunarea normala, tr reprezentand transportul, la urmatoarea cifra (poate fi 0 sau 1)
}

/// diferenta a 2 numere ( pozitive intrucat il apelez intre 2 numere de semne diferite )
void Dif(int n,char *s,char *t){
    if(Compare(n,s,t)) /// daca primul e mai mic decat al doilea, interschibam numerele, intrucat isi va pastra semnul numarul mai mare
        Swap(n,s,t);

    int i,tr=0;
    for(i=(n-1)/2-1; i>0; i--){
        if(s[i]-t[i]-tr>=0){
            s[i]='0'+s[i]-t[i]-tr;
            tr=0;
        }
        else{
            s[i]='0'+10+s[i]-t[i]-tr;
            tr=1;
        }
    } /// scaderea normala, tr reprezentand transportul cu care se scade la urmatoarea cifra (poate fi 0 sau 1)
}

int main()
{
    int n;
    char s[DIM],t[DIM],c;
    
    scanf("%d",&n);/// citirea dimensiunii vectorului
    scanf("%s\n",s); /// citirea primului numar
    c=getchar(); /// citirea primei operatii

    /// daca ultima operatie nu e 0, citim mai departe
    while(c!='0'){
        /// urmatorul numar cu care se va aplica ultima operatie citita
        scanf("%s\n",t);
        if(c=='-') Change_sings(n,t); /// facem din scadere, adunare

        if(s[0]==t[0]){ ///partea reala
            Sum(n,s,t); /// daca sunt semne identice, se aduna numerele normal si se pastreaza semnul
        }
        else{
            Dif(n,s,t); /// daca sunt semne diferite, apelam functia diferenta
        }

        if(s[(n-1)/2]==t[(n-1)/2]){ ///partea imaginara
            Sum(n,s+(n-1)/2,t+(n-1)/2); /// daca sunt semne identice, se aduna numerele normal si se pastreaza semnul
        }
        else{
            Dif(n,s+(n-1)/2,t+(n-1)/2); /// daca sunt semne diferite, apelam functia diferenta
        }
        /// toate functiile de mai sus modifica numarul s, ca fiind rezultat al ultimei operatii efectuate 
        puts(s); /// afisam ultima operatie efectuata
        c=getchar(); /// citim urmatoarea operatie
    }

    return 1;
}