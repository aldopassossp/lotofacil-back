#!/bin/bash

# Script para iniciar o sistema Lotofácil Analyzer

echo "Iniciando o sistema Lotofácil Analyzer..."

# Verificar se o MySQL está instalado
if ! command -v mysql &> /dev/null; then
    echo "MySQL não encontrado. Instalando..."
    sudo apt-get update
    sudo apt-get install -y mysql-server
    sudo systemctl start mysql
    sudo systemctl enable mysql
    
    # Configurar banco de dados
    echo "Configurando banco de dados..."
    sudo mysql -e "CREATE DATABASE IF NOT EXISTS lotofacil;"
    sudo mysql -e "CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';"
    sudo mysql -e "GRANT ALL PRIVILEGES ON lotofacil.* TO 'root'@'localhost';"
    sudo mysql -e "FLUSH PRIVILEGES;"
else
    echo "MySQL já está instalado."
    # Verificar se o banco de dados existe
    if ! sudo mysql -e "USE lotofacil;" 2>/dev/null; then
        echo "Criando banco de dados lotofacil..."
        sudo mysql -e "CREATE DATABASE IF NOT EXISTS lotofacil;"
        sudo mysql -e "CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';"
        sudo mysql -e "GRANT ALL PRIVILEGES ON lotofacil.* TO 'root'@'localhost';"
        sudo mysql -e "FLUSH PRIVILEGES;"
    fi
fi

# Iniciar o backend
echo "Iniciando o backend Spring Boot..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
echo "Backend iniciado com PID: $BACKEND_PID"

# Aguardar o backend iniciar
echo "Aguardando o backend iniciar..."
sleep 10

# Iniciar o frontend
echo "Iniciando o frontend React..."
cd ../frontend/lotofacil-frontend
npm start &
FRONTEND_PID=$!
echo "Frontend iniciado com PID: $FRONTEND_PID"

echo "Sistema Lotofácil Analyzer iniciado com sucesso!"
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:3000"
echo "Swagger UI: http://localhost:8080/swagger-ui.html"

# Função para encerrar os processos ao sair
cleanup() {
    echo "Encerrando o sistema..."
    kill $BACKEND_PID
    kill $FRONTEND_PID
    echo "Sistema encerrado."
    exit 0
}

# Capturar sinais de interrupção
trap cleanup SIGINT SIGTERM

# Manter o script em execução
echo "Pressione Ctrl+C para encerrar o sistema."
wait
