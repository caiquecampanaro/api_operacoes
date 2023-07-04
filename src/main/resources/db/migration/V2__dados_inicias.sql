insert into conta ( saldo, numero, digito, tipo_conta,limite_credito)
values (10000, 155256, 9, 'PADRAO', 0), (2000, 24242424, 24, 'ESPECIAL', 100), (5000, 76451232, 6, 'PADRAO', 0); 
insert into transferencia (id_origem, id_destino, valor, operacoes ,  quantidade_parcelas,valor_parcela,ultima_parcela)
values (2,1,1000,'AVISTA',0 ,0,0), (2,3,1000,'PARCELADA', 2,500,500),(1,2,1000,'REVERTIDA',0,0,0);