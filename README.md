



### Résultat des tests
<img width="1270" height="674" alt="1" src="Capture d'écran 2026-10-12 142547.png" />


## Résultat affiché dans la console

```
[CONTAINER] DAO instancié : dao.DaoImpl
[CONTAINER] Métier instancié : metier.MetierImpl
[DAO] Récupération de la valeur depuis la source de données...
Valeur DAO = 100.0
[DAO] Récupération de la valeur depuis la source de données...
[METIER] Calcul effectué. Résultat : 200.0
Résultats = 200.0

Process finished with exit code 0
```

## Explication du résultat

1. `config.txt` est lu et fournit `dao.DaoImpl` et `metier.MetierImpl`.
2. Les deux classes sont instanciées par réflexion (`Class.forName`).
3. Le DAO est injecté dans le métier via `setDao` (réflexion).
4. `getValue()` retourne **100.0** (`Valeur DAO = 100.0`).
5. `calcul()` appelle `getValue()` et multiplie le résultat par 2, soit **200.0** (`Résultats = 200.0`).

## Capture d'écran



<img width="1270" height="674" alt="1" src="Capture d'écran 2026-10-12 134241.png" />
