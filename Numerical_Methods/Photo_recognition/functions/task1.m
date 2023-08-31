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
## @deftypefn {} {@var{retval} =} task1 (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2021-09-14

function new_X = task1(photo, k)
[m, n] = size(photo);
  
% initializare
new_X = zeros(m, n);
  
% convertire
photo = double(photo);
  
% svd pe matricea photo
[U, S, V] = svd(photo);
  
% matricile reduse U, S, V
new_U = U(:, 1:k);
new_S = S(1:k, 1:k);
new_V = V(:, 1:k);
  
% aproximare matrice initiala
new_X = new_U * new_S * new_V';
  
new_X = uint8(new_X);
endfunction
