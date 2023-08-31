#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>

/// citirea datelor de intrare
void Read(int *v, int dim)
{
    int i;
    for(i=0; i<dim ; i++){
        scanf("%d",&v[i]);
    }
}

void Create_Cycles(int *v, int dim)
{
    /// vector caracteristic in care : 0 -> neverificat , 1 -> verificat 
    bool *checked;
    /// nr_cycles este numarul de cicluri, iar cycles este o matrice alocata dinamic in care pe randuri e numarul de cicluri, iar pe coloane, elementele unui anumit ciclu
    int nr_cycles=0,**cycles;
    checked = (bool *)calloc( dim, sizeof(bool) );
    cycles = (int **)malloc( dim * sizeof(int*) );
    
    int i;
    bool ans=1; /// pana gasim un ciclu de lungime > dim/2, raspunsul e Da, altfel nu
    for(i=0 ; i<dim ; i++){
        /// daca elementul i este neverificat, inseamna ca il putem considera inceputul unui ciclu
        if(!(checked[i]))
        {
            int x=i;
            /// aloc dinamic dimensiunea maxima a unui ciclu
            /// initiez ciclul, inceputul fiind i
            checked[x]=1;
            cycles[nr_cycles]=(int *)malloc( (dim+1)*sizeof(int) );
            cycles[nr_cycles][0]=1;
            cycles[nr_cycles][1]=x+1;
            
            /// parcurg ciclul cat timp nu ajungem de unde am plecat (nu a fost deja verificat)
            while(!(checked[v[x]-1])){
                x=v[x]-1;
                checked[x]=1;
                cycles[nr_cycles][++cycles[nr_cycles][0]]=x+1;
            }
            /// se observa ca lucram cu vectori indexati de la 0, asadar adaugam elementele in ciclu cu o unitate mai mare

            if(cycles[nr_cycles][0]>dim/2) ans=0; /// raspunsul devine Nu
            nr_cycles++;
        }
    }
    
    /// Afisam in functie de ans
    if(ans) printf("Da\n");
    else printf("Nu\n");
    
    /// Afisam ciclurile
    for(i=0; i<nr_cycles; i++){
        int j;
        for(j=1; j<cycles[i][0]; j++)
        {
            printf("%d ",cycles[i][j]);
        }
        printf("%d\n",cycles[i][cycles[i][0]]);
    }
}

int main()
{
    const int DIM=500;
    int P,v[DIM];
    
    /// numarul de cutii
    scanf("%d",&P);

    Read(v,P);
    Create_Cycles(v,P);

    return 1;
}