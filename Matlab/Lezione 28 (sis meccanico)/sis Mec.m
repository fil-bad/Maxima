%% Calcolo simulazione del sistema a 1 Dof tramite spazio di stato
clc
m=2; k=1/2; d=1/4;

%     |qDot|        |qDotDot|
% x = |    | xDot = |       |
%     |q   |        |qDot   |

A = [-d/m  -k/m
       1     0];
B = [1/m ; 0];
C = eye(2);
D = 0;

SYS = ss(A,B,C,D)
SYS.OutputName = {'qDot',' q'};
figure(1)
title("Sys 1DOF")
subplot(1,2,1);
step(SYS)
grid on

subplot(1,2,2);
impulse(SYS)
grid on

%% Calcolo simulazione del sistema a 2 Dof tramite spazio di stato
% da capire come fare

