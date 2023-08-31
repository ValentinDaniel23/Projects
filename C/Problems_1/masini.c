#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#define pret_benzina 8.02
#define pret_motorina 9.29
#define pret_hibrid 8.02
#define pret_electrica 0

void Cerinta_a(char **combustibil,int *nr,int n)
{
    int i;
    for(i=0; i<n; i++){
        if(strcmp("benzina",combustibil[i])==0) nr[0]++;
        else if(strcmp("motorina",combustibil[i])==0) nr[1]++;
        else if(strcmp("hibrid",combustibil[i])==0) nr[2]++;
        else nr[3]++;
    } /// contorizez pentru fiecare tip de combustibil
}

void Cerinta_b(char **brand,char **combustibil,double *consum,int *km,char **apar,double *consum_total,double *pret,int n,int *nr)
{
    int i,j;

    for(i=0; i<n; i++){
        bool OK=0;
        double cs_total=consum[i]*km[i]/100;

        for(j=0; j<*nr; j++){ /// trec prin brandurile deja existente
            if(strcmp(apar[j],brand[i])==0) /// verific daca exista deja brand[i]
            {
                OK=1;
                consum_total[j]+=cs_total;
                if(strcmp("benzina",combustibil[i])==0) pret[j]+=cs_total*pret_benzina;
                else if(strcmp("motorina",combustibil[i])==0) pret[j]+=cs_total*pret_motorina;
                else if(strcmp("hibrid",combustibil[i])==0) pret[j]+=cs_total*pret_hibrid;
                else pret[j]+=cs_total*pret_electrica;
                break;
            }
        }
        if(OK==0) /// daca e prima oara cand apare acest brand, il adaugam in lista
        {
            apar[*nr]=(char *)malloc( 21 * sizeof(char) );
            strcpy(apar[*nr],brand[i]);
            consum_total[*nr]=cs_total;
            if(strcmp("benzina",combustibil[i])==0) pret[*nr]=cs_total*pret_benzina;
            else if(strcmp("motorina",combustibil[i])==0) pret[*nr]+=cs_total*pret_motorina;
            else if(strcmp("hibrid",combustibil[i])==0) pret[*nr]+=cs_total*pret_hibrid;
            else pret[*nr]+=cs_total*pret_electrica;
            (*nr)++;
        }
    }
}

bool Litera(char c) ///verific daca c e litera
{
    if('A'<=c&&c<='Z') return 1;
    return 0;
}

bool Numar(char c) ///verific daca c e cifra
{
    if('0'<=c&&c<='9') return 1;
    return 0;
}

void Cerinta_c(char **brand,char **numar,int n,int *poz,int *nr)
{
    int i;
    for(i=0; i<n; i++)
    {
        int dim=strlen(numar[i]);
        if(dim==6||dim==7||dim==8) /// daca dimensiunea e 6,7 sau 8, e o sansa sa fie valid
        {
            if( Litera(numar[i][dim-1])&&Litera(numar[i][dim-2])&&Litera(numar[i][dim-3]) ) /// daca ultimile 3 caractere sunt litere, e o sansa sa fie valid
            {
                if(dim==7) /// sunt doar 2 situatii cand poate fi valid, LITERA LITERA CIFRA CIFRA sau LITERA CIFRA CIFRA CIFRA
                {
                        if( ( Litera(numar[i][0])&&Litera(numar[i][1])&&Numar(numar[i][2])&&Numar(numar[i][3]) ) || ( Litera(numar[i][0])&&Numar(numar[i][1])&&Numar(numar[i][2])&&Numar(numar[i][3]) ) )
                        {}
                        else
                        {
                            poz[*nr]=i;
                            (*nr)++;
                        }

                }
                if(dim==6) /// un caz cand poate fi valid, LITERA CIFRA CIFRA
                {
                    if(Litera(numar[i][0])&&Numar(numar[i][1])&&Numar(numar[i][2]))
                    {}
                    else
                    {
                        poz[*nr]=i;
                        (*nr)++;
                    }
                }
                if(dim==8) /// un caz cand poate fi valid, LITERA LITERA CIFRA CIFRA CIFRA
                {
                    if(Litera(numar[i][0])&&Litera(numar[i][1])&&Numar(numar[i][2])&&Numar(numar[i][3])&&Numar(numar[i][4]))
                    {}
                    else
                    {
                        poz[*nr]=i;
                        (*nr)++;
                    }
                }
            }
            else
            {
                poz[*nr]=i;
                (*nr)++;
            }
        }
        else
        {
            poz[*nr]=i;
            (*nr)++;
        }
    }
}

int main()
{
    /// vectorii unde vor fi stocati datele de intrare
    char **brand;
    char **numar;
    char **combustibil;
    double *consum;
    int *km;
    int n,i;
    char aux[21]; /// auxiliar pentru a citi stringurile

    scanf("%d",&n);
  
    brand=(char **)malloc( n * sizeof(char*) );
    numar=(char **)malloc( n * sizeof(char*) );
    combustibil=(char **)malloc( n * sizeof(char *) );
    consum=(double *)malloc( n * sizeof(double) );
    km=(int *)malloc( n * sizeof(int) );

    for(i=0; i<n; i++){
        scanf("%s ",aux);
        brand[i]=(char *)malloc( (strlen(aux)+1) * sizeof(char) ); /// se aloca fix cat trebuie + terminator
        strcpy(brand[i],aux);
        scanf("%s ",aux);
        numar[i]=(char *)malloc( (strlen(aux)+1) * sizeof(char) );/// se aloca fix cat trebuie + terminator
        strcpy(numar[i],aux);
        scanf("%s ",aux);
        combustibil[i]=(char *)malloc( (strlen(aux)+1) * sizeof(char) );/// se aloca fix cat trebuie + terminator
        strcpy(combustibil[i],aux);

        scanf("%lf ",&consum[i]);
        scanf("%d\n",&km[i]);
    }

    char task;
    scanf("%c",&task);
    if(task=='a')
    {
        int *nr=(int *)calloc(4, sizeof(int)); /// un vector pentru retinerea a 4 frecvente, fiind initializat cu 0
        Cerinta_a(combustibil,nr,n);

        printf("benzina - %d\n",nr[0]);
        printf("motorina - %d\n",nr[1]);
        printf("hibrid - %d\n",nr[2]);
        printf("electric - %d\n",nr[3]);
        return 1;
    }

    if(task=='b')
    {
        char **apar=(char **)malloc( n * sizeof(char*) ); /// se retin brandurilee deja parcurse
        double *consum_total=(double *)malloc( n * sizeof(double) ),*pret=(double *)malloc( n * sizeof(double) ); /// informatiile pentru fiecare brand din apar[i]
        int nr=0;

        Cerinta_b(brand,combustibil,consum,km,apar,consum_total,pret,n,&nr);

        int i;
        for(i=0; i<nr; i++)
        {
            printf("%s a consumat %.2lf - %.2lf lei\n",apar[i],consum_total[i],pret[i]);
        }
        return 1;
    }

    if(task=='c')
    {
        int *poz=(int *)malloc( n * sizeof(int) ),nr=0; /// se retin indicii masinilor cu numar invalid
        Cerinta_c(brand,numar,n,poz,&nr);

        if(nr==0)
        {
            printf("Numere corecte!\n");
            return 1;
        }
        
        int i;
        for(i=0;i<nr;i++)
        {
            printf("%s cu numarul %s: numar invalid\n",brand[poz[i]],numar[poz[i]]);
        }
    }

    return 1;
}