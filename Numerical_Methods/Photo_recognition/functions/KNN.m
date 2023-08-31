## Copyright (C) 2021 Andrei
##
## This program is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
##
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <https://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {} {@var{retval} =} KNN (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2021-08-09

function prediction = KNN(labels, Y, test, k)
  % initializare
  prediction = 0;
  
  % initializez distantele
  [m, n] = size(Y);
  distances = zeros(m, 1);
  
  % distanta euclidiana pentru fiecare linie
  for i = 1:m
    distances(i) = norm(Y(i, :) - test);
  end
  
  % sortez distantele in ordine crescatoare si pastrez primele k valori
  [~, indices] = sort(distances);
  k_nearest_labels = labels(indices(1:k));
  
  % calculez predictia ca mediana a k-nearest neighbours
  prediction = median(k_nearest_labels);
end

