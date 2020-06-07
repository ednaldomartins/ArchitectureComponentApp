# ArchitectureComponentApp
Repositório para criação de um app usando componentes de arquitetura Android. Esse repositório é a base inicial para o aplicativo Libflix que está em um repositório privado.

### Componentes de Arquitetura Android


Abaixo serão apresentados alguns *__Componentes de Arquitetura Android__* que foram anunciados na Google I/O 2017, os quais, são capazes de tornar a arquitetura dos aplicativos componentizada. Para demonstrar a capacidade desses componentes, o aplicativo Libflix foi desenvolvido. Além disso, técnicas de Micro Frontends foram usadas, com o objetivo de construir uma aplicação Android, componentizada, , escalável e testável.

Esses componentes podem trazer diversos benefícios. As principaiscaracterísticas desses componentes são [2]:

* **LifeCycle**: Gerencia o ciclo de vida do aplicativo, auxiliando os componentes Activity e Fragment a manterem as suas configurações salvas através da ViewModel, sendo assim, evitando perda de dados, e tornando o armazenamento ou o carregamento dessas informações mais seguro [15].

* **LiveData**: Deve ser usado para criar objetos que podem notificar seus observadores quando eles são alterados, ou quando há alterações feitas no banco de dados [15]. Esse componente trabalha em conjunto com o LifeCycle, reconhecendo o ciclo de vida da aplicação [8].

* **ViewModel**: É responsável por armazenar os dados presentes na interface de usuário, que não são descartados quando um aplicativo é rotacionado, ou para guardar dados que não estejam relacionados às configurações da Activity e Fragment [15].

* **Room**: Guarda os dados das aplicações em execução em cache no armazenamento do dispositivo [9]. Essa ferramenta faz um mapeamento de objetos SQLite e pode ser utilizado para retornar um Objeto do tipo LiveData [15].

### Arquitetura do Aplicativo

![image](https://user-images.githubusercontent.com/21205709/83975004-99c4ef00-a8c7-11ea-83ea-40a5262712f0.png)

### Referências

[2] [Android Developers: Guide to App Architecture](https://developer.android.com/jetpack/docs/guide)

[8] [Android Developers: Visão Geral do LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

[9] [Android Developers: Salvar dados em um banco de dados local usando o Room](https://developer.android.com/training/data-storage/room/index.html)

[15] [Android Developers: Componentes da Arquitetura do Android](https://developer.android.com/topic/libraries/architecture)
