function [Y, InitialMatrix] = parse_csv_file(file_path)
  fid = fopen(file_path);
  % Am grija sa extrag corect matricea din fisier cu elementele char
   InitialMatrix = textscan(fid, '%s', 'Delimiter', ',');
   fclose(fid);

   % Stiu ca avem 13 coloane
   num_cols = 13;

   data_size = length(InitialMatrix{1});
   num_rows = data_size / num_cols;
   InitialMatrix = reshape(InitialMatrix{1}, num_cols, num_rows)';
   % Prima linie definea coloanele
   InitialMatrix = InitialMatrix(2:num_rows, 1:num_cols);
   num_rows--;

   Y = zeros(num_rows, 1);
   
   % Y il fac in vector coloana de double
   for i=1:num_rows
     Y(i, 1) = str2double( InitialMatrix{i, 1} );
   endfor
   
   % Scot Y din InitialMatrix
   InitialMatrix = InitialMatrix(1:num_rows, 2:num_cols);
   num_cols--;
   
   for i = 1:num_rows
     for j = 1:num_cols
       % Daca e double, nu returneaza nan functia str2double
       if ~isnan(str2double(InitialMatrix(i, j)))
              % Tranform doar ce e double
              InitialMatrix(i, j) = str2double(InitialMatrix(i, j));
       end
     endfor
   endfor
endfunction
