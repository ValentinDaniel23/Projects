EXECUTABLE = tema3
SOURCE_FILES = tema3.c
OUTPUT_FILE = output.txt

.PHONY: all build run clean

all: build

build:
	gcc $(SOURCE_FILES) -o $(EXECUTABLE)

run:
	./$(EXECUTABLE) > $(OUTPUT_FILE)

clean:
	rm -f $(EXECUTABLE) $(OUTPUT_FILE)
