## Copyright (C) 2023 Andrei
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
## @deftypefn {} {@var{retval} =} task2 (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2023-02-28

function new_X = task2(photo, pcs)
[m, n] = size(photo);

% convertire
photo = double(photo);

% vector coloana ce calculeaza media pe rand
miu = mean(photo, 2);

% actualizare
photo = photo - miu;

% matricea Z
Z = photo' / sqrt(n - 1);

% SVD pentru matricea Z
[U, S, V] = svd(Z);

% spatiul k-dimensional
W = V(:, 1:pcs);

% proiectia lui A in spatiul componentelor principale
Y = W' * photo;

% aproximare matrice initiala
new_X = W * Y + miu;

new_X = uint8(new_X);
endfunction