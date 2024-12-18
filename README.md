<h1>Payment System</h1>

<h2>Descrição</h2>
<p>O Payment System é uma API desenvolvida em Java que consome uma API externa do banco Efí para realizar operações relacionadas ao sistema de pagamentos via Pix. Este sistema também integra webhooks, permitindo o recebimento de notificações automáticas sobre eventos de pagamento.</p>

<h2>Funcionalidades</h2>
<h3>Operações com Pix</h3>
<ul>
    <li><strong>Gerar chaves Pix aleatórias:</strong> Cria chaves Pix aleatórias que podem ser usadas para pagamentos.</li>
    <li><strong>Listar chaves Pix cadastradas:</strong> Retorna todas as chaves Pix registradas no sistema.</li>
    <li><strong>Excluir chaves Pix:</strong> Remove uma chave Pix previamente cadastrada.</li>
    <li><strong>Gerar cobranças via QR Code:</strong> Cria um QR Code contendo os dados de pagamento.</li>
    <li><strong>Gerar imagens PNG do QR Code para pagamentos:</strong> Gera a imagem do QR Code que pode ser compartilhada ou exibida.</li>
</ul>

<h3>Webhooks</h3>
<ul>
        <li><strong>Receber notificações de pagamento:</strong> Recebe notificações de confirmação de pagamentos.</li>
        <li><strong>Registrar webhooks:</strong> Associar uma URL de webhook a uma chave Pix.</li>
        <li><strong>Listar webhooks registrados:</strong> Consultar webhooks cadastrados em um intervalo de datas.</li>
        <li><strong>Excluir webhooks:</strong> Desassociar uma URL de webhook de uma chave Pix.</li>
    </ul>

  <h2>Tecnologias Utilizadas</h2>
    <ul>
      <li><strong>Linguagem:</strong> Java</li>
      <li><strong>Framework:</strong> Spring Boot</li>
      <li><strong>Gerenciamento de Dependências:</strong> Maven</li>
      <li><strong>Segurança:</strong> Autenticação mTLS (mutual TLS) conforme exigência do Banco Central.</li>
    </ul>

   <h2>Configuração de Ambiente</h2>
    <p>Para garantir o funcionamento correto do sistema, é necessário configurar o uso de certificados para ambientes de produção e homologação:</p>
    <ul>
        <li><strong>Certificado de Produção:</strong> Usado para operações reais com Pix.</li>
        <li><strong>Certificado de Homologação:</strong> Usado para testes de integração e operações simuladas, como o registro e recebimento de webhooks.</li>
    </ul>

   <h3>Requisitos</h3>
    <ol>
        <li><strong>Ambiente local ou exposto via Ngrok (para testes):</strong> Para receber notificações via webhook, você pode utilizar o Ngrok configurado com a flag <code>X-Skip-mTLS-Check</code>.</li>
        <li><strong>Domínio próprio:</strong> Opcionalmente, é possível usar um domínio personalizado configurado via DNS para redirecionar o tráfego do Ngrok.</li>
    </ol>

<h2>Contato</h2>
<p>Se tiver dúvidas ou sugestões, entre em contato pelo email: <code>cauaconceicaodev@hotmail.com</code>.</p>
