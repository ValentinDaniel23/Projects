#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "structs.h"

extern void get_operations(void (*operations[])(void *));

// Functia returneaza 1 daca senzorul, fie el TIRE sau PMU,
// se incadreaza in limitele normale si 0 in caz contrar
int verifica(enum sensor_type sensor_type, void *data) {
  if (sensor_type == TIRE) {
    if (((tire_sensor *)data)->pressure < 19.0 ||
        ((tire_sensor *)data)->pressure > 28.0)
      return 0;
    if (((tire_sensor *)data)->temperature < 0.0 ||
        ((tire_sensor *)data)->temperature > 120.0)
      return 0;
    if (((tire_sensor *)data)->wear_level < 0 ||
        ((tire_sensor *)data)->wear_level > 100)
      return 0;
  }
  if (sensor_type == PMU) {
    if (((power_management_unit *)data)->voltage < 10.0 ||
        ((power_management_unit *)data)->voltage > 20.0)
      return 0;
    if (((power_management_unit *)data)->current < -100.0 ||
        ((power_management_unit *)data)->current > 100.0)
      return 0;
    if (((power_management_unit *)data)->power_consumption < 0.0 ||
        ((power_management_unit *)data)->power_consumption > 1000.0)
      return 0;
    if (((power_management_unit *)data)->energy_regen < 0 ||
        ((power_management_unit *)data)->energy_regen > 100)
      return 0;
    if (((power_management_unit *)data)->energy_storage < 0 ||
        ((power_management_unit *)data)->energy_storage > 100)
      return 0;
  }

  return 1;
}

int sensorcmp(const void *a, const void *b) {
  return ((sensor *)b)->sensor_type - ((sensor *)a)->sensor_type;
}

int main(int argc, char const *argv[]) {
  if (argc < 2) {
    printf("Introdu fisier binar\n");
    return 1;
  }

  // Deschiderea fisierului binar
  FILE *f;
  f = fopen(argv[1], "rb");

  if (f == NULL) {
    printf("Eroare!\n");
    return 1;
  }

  int Sensors, i;
  fread(&Sensors, sizeof(int), 1, f);

  sensor *S = (sensor *)malloc(Sensors * sizeof(sensor));

  // Citesc datele din fisier cu fread
  // Am grija la citirea tipului de date
  for (i = 0; i < Sensors; i++) {
    fread(&S[i].sensor_type, sizeof(enum sensor_type), 1, f);

    if (S[i].sensor_type == TIRE) {
      S[i].sensor_data = (tire_sensor *)malloc(sizeof(tire_sensor));
      fread(S[i].sensor_data, sizeof(tire_sensor), 1, f);
    } else if (S[i].sensor_type == PMU) {
      S[i].sensor_data =
          (power_management_unit *)malloc(sizeof(power_management_unit));
      fread(S[i].sensor_data, sizeof(power_management_unit), 1, f);
    }

    fread(&S[i].nr_operations, sizeof(int), 1, f);
    S[i].operations_idxs = (int *)malloc(S[i].nr_operations * sizeof(int));
    fread(S[i].operations_idxs, sizeof(int), S[i].nr_operations, f);
  }

  // Sortez cu qsort
  // Se putea si dintr-o parcurgere ordonand printr-un vector auxiliar,sau
  // chiar in acelasi vector S, calculand mai intai numarul din fiecare tip
  qsort(S, Sensors, sizeof(sensor), sensorcmp);
  void (*operations[8])(void *);
  get_operations(operations); // Vectorul de operatii (pointeri la functii)

  char Command[20];
  while (scanf("%s", Command)) {
    int indx;
    if (strcmp(Command, "exit") == 0) {
      // Daca comanda este exit, eliberez memoria si opresc
      for (i = 0; i < Sensors; i++) {
        free(S[i].sensor_data);
        free(S[i].operations_idxs);
      }
      free(S);
      break;
    } else if (strcmp(Command, "print") == 0) {
      // Daca comanda e print, afisez daca se afla in limite pozitia
      scanf(" %d", &indx);
      if (indx < 0 || indx >= Sensors) {
        printf("Index not in range!\n");
        continue;
      }

      if (S[indx].sensor_type == PMU) {
        printf("Power Management Unit\n");
        printf("Voltage: %.2f\n",
               ((power_management_unit *)S[indx].sensor_data)->voltage);
        printf("Current: %.2f\n",
               ((power_management_unit *)S[indx].sensor_data)->current);
        printf(
            "Power Consumption: %.2f\n",
            ((power_management_unit *)S[indx].sensor_data)->power_consumption);
        printf("Energy Regen: %d%%\n",
               ((power_management_unit *)S[indx].sensor_data)->energy_regen);
        printf("Energy Storage: %d%%\n",
               ((power_management_unit *)S[indx].sensor_data)->energy_storage);
      }

      if (S[indx].sensor_type == TIRE) {
        printf("Tire Sensor\n");
        printf("Pressure: %.2f\n",
               ((tire_sensor *)S[indx].sensor_data)->pressure);
        printf("Temperature: %.2f\n",
               ((tire_sensor *)S[indx].sensor_data)->temperature);
        printf("Wear Level: %d%%\n",
               ((tire_sensor *)S[indx].sensor_data)->wear_level);
        if (((tire_sensor *)S[indx].sensor_data)->performace_score == 0)
          printf("Performance Score: Not Calculated\n"); // Necalculata
        else
          printf("Performance Score: %d\n",
                 ((tire_sensor *)S[indx].sensor_data)->performace_score);
      }
    } else if (strcmp(Command, "analyze") == 0) {
      // La comanda analyze parcurg in ordinea data operatiile
      // cu pointerul la functii
      scanf(" %d", &indx);
      if (indx < 0 || indx >= Sensors) {
        printf("Index not in range!\n");
        continue;
      }

      for (i = 0; i < S[indx].nr_operations; i++) {
        operations[S[indx].operations_idxs[i]](S[indx].sensor_data);
      }
    } else if (strcmp(Command, "clear") == 0) {
      // La comanda clear am grija sa pastrez la stanga senzorii
      // valabili, lasand la dreapta pe cei pe care ii scot
      int new_Sensors = 0;
      for (i = 0; i < Sensors; i++) {
        if (verifica(S[i].sensor_type, S[i].sensor_data) == 1) {
          if (i != new_Sensors) {
            sensor aux = S[new_Sensors];
            S[new_Sensors] = S[i];
            S[i] = aux;
          }
          new_Sensors++;
        }
      }
      for (i = new_Sensors; i < Sensors; i++) {
        free(S[i].sensor_data);
        free(S[i].operations_idxs);
      }
      sensor *aux = (sensor *)realloc(S, new_Sensors * sizeof(sensor));
      Sensors = new_Sensors;
      S = aux;
    }
  }

  fclose(f);
  return 0;
}
