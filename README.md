
## Application aux microservices

Dans une architecture microservices, chaque service doit être autonome et déployable indépendamment. Ce lab applique les principes suivants :

- **Découplage par interfaces** : le métier (`MetierImpl`) ne connaît que l'interface `IDao`, jamais l'implémentation concrète. Un service peut ainsi changer de source de données sans modification du code métier.
- **Configuration externalisée** : les classes à charger sont définies dans `config.txt`. En pratique, un même service peut utiliser une source locale en développement et une base distante en production, sans recompilation.
- **Déploiement indépendant** : changer d'implémentation revient à changer le fichier de configuration, ce qui évite de redéployer tout le code.
- **Bootstrapping** : la réflexion est utilisée uniquement au démarrage du service pour construire les composants. C'est le même principe que celui des conteneurs d'injection de dépendances (Spring, CDI) utilisés dans les microservices.
- **Testabilité** : chaque couche se teste séparément, en injectant un DAO fictif dans le métier, sans base de données réelle.

Limites : la réflexion coûte en performance et contourne les contrôles de compilation. Elle ne doit donc pas être utilisée dans la boucle de traitement des requêtes.

## Tests unitaires

Les tests sont dans `src/test/java/metier/MetierImplTest.java` (JUnit 4.13.2) :

| Test | Vérification |
|------|--------------|
| `testCalculAvecDaoFictif` | Avec un DAO fictif qui retourne 10.0, `calcul()` retourne 20.0 |
| `testCalculAvecDaoImpl` | Avec `DaoImpl` (100.0), `calcul()` retourne 200.0 |
| `testCalculSansDao` | Sans DAO injecté, `calcul()` lève une `IllegalStateException` |

Exécution : `mvn test` ou clic droit sur la classe de test dans IntelliJ.

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
