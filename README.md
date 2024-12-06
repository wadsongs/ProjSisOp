#### Autores
Wadson Gurgel Sátiro

Victor Lira Marques Bastos


# Simulador de Sistema de Arquivos
## Metodologia

O simulador será desenvolvido em linguagem de programação Java. Ele receberá as chamadas de métodos com os devido parâmetros. Em seguida, serão implementados os métodos correspondentes aos comandos de um SO. 

O programa executará cada funcionalidade e exibirá o resultado na tela quando necessário.

### Parte 1: Introdução ao Sistema de Arquivos com Journaling

Um sistema de arquivos é um componente essencial de sistemas operacionais que gerencia como os dados são armazenados, organizados e recuperados em dispositivos de armazenamento, como discos rígidos, SSDs, pendrives e outros meios. Ele atua como uma interface entre o usuário (ou aplicações) e o hardware de armazenamento, permitindo que arquivos e diretórios sejam manipulados de forma estruturada.

O journaling em sistemas de arquivos tem como propósito garantir a integridade e a consistência dos dados, especialmente em situações de falhas, como quedas de energia, erros de hardware ou travamentos do sistema operacional. Ele funciona registrando alterações planejadas antes de aplicá-las efetivamente no disco, o que permite que o sistema se recupere ou reverta operações incompletas. Sem o journaling, um sistema de arquivos pode ficar em um estado inconsistente após uma falha, exigindo processos demorados de recuperação, como verificações completas do disco. Com o journaling, a recuperação é mais eficiente, pois processa apenas as operações registradas no log.

O funcionamento do journaling é semelhante a um log de transações, onde operações críticas no sistema de arquivos, como criar, mover ou excluir arquivos, são registradas antes de serem aplicadas. O processo envolve registrar a operação planejada no journal, confirmar que o registro foi gravado, executar a operação real no sistema de arquivos e atualizar ou remover o registro no journal. Em caso de falha durante a operação, o journal é usado para completar operações pendentes ou reverter alterações parciais, garantindo a consistência do sistema.

Existem diferentes abordagens de journaling. O Write-Ahead Logging registra todas as operações planejadas antes de aplicá-las no sistema de arquivos, garantindo alta integridade, mas com impacto moderado no desempenho. O Ordered Journaling registra apenas os metadados no journal e grava os dados diretamente no disco antes dos metadados, proporcionando melhor desempenho, mas com menor proteção contra perda de dados. O Data Journaling registra tanto os dados quanto os metadados no journal, oferecendo maior proteção, mas reduzindo o desempenho. O Log-Structured Journaling adota uma abordagem onde todo o sistema de arquivos funciona como um grande journal, otimizando gravações em dispositivos de armazenamento sequencial, mas pode impactar a leitura. Já o Metadata-Only Journaling registra apenas os metadados, equilibrando desempenho e integridade, embora os dados possam ser corrompidos em caso de falha.

O journaling oferece benefícios importantes, como recuperação rápida, maior confiabilidade e consistência garantida, especialmente para metadados. No entanto, ele introduz um custo de desempenho devido às operações extras no journal. Sistemas modernos, como o ext4, permitem configurações flexíveis, ajustando o nível de journaling para equilibrar segurança e desempenho. O tipo de journaling escolhido deve considerar as necessidades específicas do sistema, como prioridade de desempenho, criticidade dos dados e tempo de recuperação após falhas.

## Parte 2: Arquitetura do Simulador

No sistema de arquivos representado, as estruturas de dados são modeladas para simular a organização e manipulação de arquivos e diretórios em um ambiente hierárquico. Embora seja uma simulação, ela reflete conceitos fundamentais de sistemas reais. As principais estruturas de dados utilizadas são as seguintes:

* Diretório Raiz (Root Directory)
> Representado pela classe FileSystemSimulator, o diretório raiz é o ponto de partida para todas as operações no sistema de arquivos.
Armazenado como um objeto File Java (rootDirectory), ele simula o diretório principal onde todos os outros diretórios e arquivos são criados.
Proporciona uma hierarquia organizada, permitindo que arquivos e subdiretórios sejam acessados de forma relativa ou absoluta.

* Arquivos
> Cada arquivo no sistema é representado como um objeto da classe File Java.
Os arquivos são identificados por seus nomes e caminhos no sistema de arquivos.
O conteúdo do arquivo é gerenciado por operações de leitura e escrita, como no método createFile, que usa BufferedWriter para gravar dados no arquivo físico simulado.

* Diretórios
>Diretórios são tratados como objetos File que contêm outros arquivos e/ou subdiretórios.
A estrutura hierárquica de diretórios é mantida por meio de caminhos relativos ou absolutos, simulando a organização de um sistema de arquivos real.
A listagem do conteúdo de um diretório é realizada usando o método listDirectory, que chama list() ou listFiles() na instância do diretório.

* Journal (Log de Operações)
>O journaling é representado por um arquivo físico (journal.log), usado para registrar todas as operações realizadas no sistema de arquivos.
Cada operação, como criação, exclusão, renomeação ou cópia, é registrada no log com detalhes (caminho, tipo de operação, status).
O journal atua como uma estrutura linear de registros, armazenada no disco, que pode ser consultada para recuperação de operações em caso de falha.

* Métodos para Operações do Sistema de Arquivos
>Os métodos, como createFile, createDirectory, deleteFileOrDirectory, rename e copyFile, operam diretamente sobre as estruturas de dados File e atualizam o estado do sistema.
Essas operações manipulam os metadados dos arquivos (nome, localização) e, em alguns casos, o conteúdo, enquanto interagem com o journal para garantir consistência.

* Recursão para Deleção
>Para excluir diretórios, é usada uma abordagem recursiva (método deleteRecursive) que percorre o conteúdo do diretório antes de removê-lo. Isso reflete como sistemas reais lidam com estruturas hierárquicas.

## Parte 3: Implementação em Java

Classe "FileSystemSimulator": Implementa o simulador do sistema de arquivos, incluindo métodos para cada operação.

Classes File e Directory: Representam arquivos e diretórios.

Classe Journal: Gerencia o log de operações.
