function [FeatureMatrix] = prepare_for_regression(InitialMatrix)
  
  [n, m] = size(InitialMatrix);
  FeatureMatrix = [];
  Aux = zeros(n, 1);
  for i = 1:m
    if isfloat(InitialMatrix{1, i})
      FeatureMatrix = [FeatureMatrix, cell2mat(InitialMatrix(:, i))]; % Adaug direct coloana
    else
      if strcmp(InitialMatrix{1, i}, "yes") || strcmp(InitialMatrix{1, i}, "no") % In functie de mesaj, adaug coloane de 0 si/sau 1
        for j = 1:n
          if strcmp(InitialMatrix{j, i}, "yes")
            Aux(j, 1) = 1;
          else
            Aux(j, 1) = 0;
          endif
        endfor
        FeatureMatrix = [FeatureMatrix, Aux];
      else
        for j = 1:n
          if strcmp(InitialMatrix{j, i}, "semi-furnished")
            Aux(j, 1) = 1;
          else
            Aux(j, 1) = 0;
          endif
        endfor
        FeatureMatrix = [FeatureMatrix, Aux];
        for j = 1:n
          if strcmp(InitialMatrix{j, i}, "unfurnished")
            Aux(j, 1) = 1;
          else
            Aux(j, 1) = 0;
          endif
        endfor
        FeatureMatrix = [FeatureMatrix, Aux];
      endif
    endif
  endfor

endfunction