#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int limit=10000;

void CodificareA(char *s,char *template)
{
    char **matrix;
    int n=strlen(s),m=strlen(template);
    int nn=n/m+(n%m!=0),i,j; /// nn e numarul de linii a matricei din cerinta A

    matrix=(char **)malloc( nn * sizeof(char *) );
    for(i=0; i<n; i++){
        if(i%m==0)
        {
            matrix[i/m]=(char *)malloc( m * sizeof(char) ); /// aloc memorie la fiecare rand nou
        }
        matrix[i/m][i%m]=s[i]; /// i/m si i%m reprezinta randul, respectiv coloana asociata lui i
        /// adaug textul s in matrice, caracter cu caracter; se observa ca se schimba randul daca indicele i are restul 0 la impartirea cu m
    }
    for(i=n; i<nn*m; i++){
        matrix[i/m][i%m]=' ';
        /// completez restul matricei ( observam ca nu va mai fi vorba sa alocam alta memorie )
    }

    int *permutation; /// asociem cheii (template), o permutare, pe care o vom sorta dupa literele asociate
    permutation=(int *)malloc( m * sizeof(int) );
    for(i=0; i<m; i++)
        permutation[i]=i;

    for(i=0; i<m-1; i++){ /// sortarea ceruta
        for(j=i+1; j<m; j++){
            if(template[permutation[i]]>template[permutation[j]]) /// sortam dupa literele asociate
            {
                int aux=permutation[i];
                permutation[i]=permutation[j];
                permutation[j]=aux;
            }
        }
    }

    int z=0;
    for(j=0; j<m; j++){
        for(i=0; i<nn; i++){
            s[z++]=matrix[i][permutation[j]]; /// reformam sirul s, dupa matricea pe care o vom parcurge pe coloane, in ordinea data de permutare
        }
    }
    s[z]='\0'; /// punem un terminator in sir
}

/// toate variabilele au aceeasi semnificatie ca in functia CodificareA
void DecodificareA(char *s,char *template)
{
    char **matrix;
    int n=strlen(s),m=strlen(template);
    int nn=n/m+(n%m!=0),i,j;
    
    matrix=(char **)malloc( nn * sizeof(char *) );
    for(i=0; i<nn; i++){
        matrix[i]=(char *)malloc( m * sizeof(char) );
    }

    for(i=0; i<n; i++){
        matrix[i%nn][i/nn]=s[i]; /// Cum decodificarea reprezinta procesul invers, inseamna ca introducem sirul pe coloane
    }
    for(i=n; i<nn*m; i++){
        matrix[i%nn][i/nn]=' '; /// Completam sirul
    }
    
    
    int *permutation;
    permutation=(int *)malloc( m * sizeof(int) );
    for(i=0; i<m; i++){
        permutation[i]=i;
    }

    for(i=0; i<m-1; i++){
        for(j=i+1; j<m; j++){
            if(template[permutation[i]]>template[permutation[j]])
            {
                int aux=permutation[i];
                permutation[i]=permutation[j];
                permutation[j]=aux;
            }
        }
    } /// 
    
    int *poz; /// poz reprezinta pozitia unei litere din template, in template ul sortat
    poz=(int *)malloc( m * sizeof(int) );
    for(i=0;i<m;i++){
     poz[permutation[i]]=i;
    }

    int z=0;
    for(i=0; i<nn; i++){
        for(j=0; j<m; j++){
            s[z++]=matrix[i][poz[j]]; /// reformam sirul s, dupa matricea pe care o vom parcurge pe linii, in ordinea data de poz, template ul initial nesortat
        }
    }
    s[z]='\0'; /// punem un terminator in sir
}

/// Functia next char, transmite urmatorul char, pornind de la c, cu n pozitii
char NextChar(char c,int n)
{
    if( c>='a' )
    {
        if( n <= 'z'-c )
        {
            c+=n; /// nu trece de z
        }
        else if( n <= 'z'-c+26 )
        {
            c='A'+(n-1-('z'-c)); /// nu trece de Z
        }
        else
        {
            c='a'+(n-27-('z'-c)); /// undeva intre 'a' si c
        }
    }
    else
    {
        if( n <= 'Z'-c )
        {
            c+=n; /// nu trece de Z
        }
        else if( n <= 'Z'-c+26 )
        {
            c='a'+(n-1-('Z'-c)); /// nu trece de z
        }
        else
        {
            c='A'+(n-27-('Z'-c)); /// undeva intre 'A' si c
        }
    }
    return c;
}

void CodificareB(char *s,int *nr)
{
    if( *nr != 0 ) /// daca nr este 0 -> nicio modificare
    {
        char *t;  /// vector auxiliar in care mut cele nr caractere
        t=(char *)malloc( *nr * sizeof(char) );
        int i,n=strlen(s);
        for(i=0; i<*nr; i++){
            t[i]=s[n-*nr+i]; /// retin ultimile nr caractere in ordine
        }
        for(i=n-*nr-1; i>=0; i--){
            s[i+*nr]=s[i]; /// permut sirul cu nr pozitii la dreapta
        }
        for(i=0; i<*nr; i++){
            s[i]=t[i]; /// pun cele nr numere din t in primele nr pozitii in s
        }

        for(i=0; i<*nr; i++){
            if( (s[i]>='a'&&s[i]<='z')||(s[i]>='A'&&s[i]<='Z') )
            {
                s[i]=NextChar(s[i],(*nr)%52); /// cum intre a si z si A si Z sunt in total 52 de caractere, se va ajunge de unde ai plecat dupa 52 de pozitii, asadar e de ajuns sa luam 52%(*nr)
            }
        }
    }
}

/// toate variabilele au aceeasi semnificatie ca in functia CodificareB
void DecodificareB(char *s,int *nr)
{
    if( *nr != 0 )
    {
        int i,n=strlen(s);
        for(i=0; i<(*nr); i++){
            if( (s[i]>='a'&&s[i]<='z')||(s[i]>='A'&&s[i]<='Z') )
            {
                s[i]=NextChar(s[i],52-(*nr)%52); /// ca in codificare, dupa 52 permutari la stanga se ajunge de unde ai plecat; ca sa facem din din permutare la stanga -> la dreapta, fiind un ciclu de lungime 52, vom permuta la dreapta cu 52-(*nr)%52  
            }
        }

        char *t;
        t=(char *)malloc( *nr * sizeof(char) );
        for(i=0; i<*nr; i++){
            t[i]=s[i];
        }
        for(i=0; i<n-*nr; i++){
            s[i]=s[i+*nr];
        }
        for(i=0; i<*nr; i++){
            s[n-*nr+i]=t[i];
        }
        ///acelasi proces ca la CodificareB, insa nu ne mai referim la ultimile nr caractere, ci la primele nr
    }
}

int main( void )
{
    char s[limit],task[14],template[501];
    int n;
    
    /// citirea primului text pana la linie noua
    scanf("%[^\n]", s);
    
    /// citim cerintele
    while(scanf("%s ",task)){
        if(task[0]=='S') /// daca primul caracter e S, inseamna ca textul e STOP, adica s a terminat
        {
            return 1;
        }
        if(task[strlen(task)-1]=='A') /// ultima litera a cerintei indica operatia A
        {
            scanf("%[^\n]", template); /// citim modelul pe care aplic cerinta A
            if(task[0]=='C') /// daca prima litera e C, atunci e codificare
            {
                CodificareA(s,template);
            }
            else /// altfel decodificare
            {
                DecodificareA(s,template);
            }
        }
        else /// ultima litera a cerintei indica operatia B
        {
            scanf("%d",&n); /// citim argumentul pe care aplic cerinta B
            if(task[0]=='C')
            {
                CodificareB(s,&n); /// daca prima litera e C, atunci e codificare
            }
            else
            {
                DecodificareB(s,&n); /// altfel decodificare
            }
        }
        /// in sirul s, s a transmis rezultatul ultimei cerinte
        printf("%s\n",s); /// afisez rezultatul ultimei cerinte
    }

    return 1;
}

