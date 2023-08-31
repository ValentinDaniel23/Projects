function [Y, InitialMatrix] = parse_data_set_file(file_path)
  fout = fopen(file_path, 'r');
    % Citesc datele de intrare cu delimitator de spatiu
    header = strsplit(fgetl(fout), ' ');
    m = str2double(header{1});
    n = str2double(header{2});
    InitialMatrix = cell(m, n);
    Y = zeros(m, 1);
    i = 1;
    % Continui pana ajung la finalul fisierului
    while ~feof(fout)
        line = strsplit(fgetl(fout), ' ');
        % Citesc fiecare linie avand in vedere si tipul de date
        Y(i) = str2double(line{1});
        for j = 2:numel(line)
            if ~isnan(str2double(line(1, j)))
              InitialMatrix(i, j-1) = str2double(line(1, j));
            else
              InitialMatrix(i, j-1) = line(1, j);
            end
        end
        i = i + 1;
    end
    fclose(fout);
endfunction