#!/bin/bash
set -e

# Move entities
mv src/main/java/com/logonedigital/MBOAcare/entity/* src/main/java/com/Mboacare/Mboacare/entities/

# Move repositories
mv src/main/java/com/logonedigital/MBOAcare/repositoy/* src/main/java/com/Mboacare/Mboacare/repositories/

# Move DTOs
mv src/main/java/com/logonedigital/MBOAcare/dto/* src/main/java/com/Mboacare/Mboacare/dto/

# Move Services
mkdir -p src/main/java/com/Mboacare/Mboacare/services/pharmacie
mv src/main/java/com/logonedigital/MBOAcare/service/pharmaci/* src/main/java/com/Mboacare/Mboacare/services/pharmacie/
mv src/main/java/com/logonedigital/MBOAcare/service/medicament/* src/main/java/com/Mboacare/Mboacare/services/pharmacie/
mv src/main/java/com/logonedigital/MBOAcare/service/stock/* src/main/java/com/Mboacare/Mboacare/services/pharmacie/

# Move Controller
mv src/main/java/com/logonedigital/MBOAcare/controller/* src/main/java/com/Mboacare/Mboacare/controller/

# Replace package names in moved files
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/com.logonedigital.MBOAcare/com.Mboacare.Mboacare/g' {} +
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/entity/entities/g' {} +
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/repositoy/repositories/g' {} +
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/service.pharmaci/services.pharmacie/g' {} +
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/service.medicament/services.pharmacie/g' {} +
find src/main/java/com/Mboacare/Mboacare -name "*.java" -exec sed -i 's/service.stock/services.pharmacie/g' {} +

# Clean up old structure
rm -rf src/main/java/com/logonedigital

echo "Done moving files"
