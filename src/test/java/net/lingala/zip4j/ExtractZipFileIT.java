package net.lingala.zip4j;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.utils.TestUtils;
import net.lingala.zip4j.utils.ZipFileVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractZipFileIT extends AbstractIT {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testExtractAllStoreAndNoEncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = new ZipParameters();
    zipParameters.setCompressionMethod(CompressionMethod.STORE);
    ZipFile zipFile = new ZipFile(generatedZipFile);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllStoreAndZipStandardEncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.ZIP_STANDARD, null);
    zipParameters.setCompressionMethod(CompressionMethod.STORE);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllStoreAndAes128EncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_128);
    zipParameters.setCompressionMethod(CompressionMethod.STORE);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllStoreAndAes256EncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_256);
    zipParameters.setCompressionMethod(CompressionMethod.STORE);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllDeflateAndNoEncryptionExtractsSuccessfully() throws ZipException {
    ZipFile zipFile = new ZipFile(generatedZipFile);
    zipFile.addFiles(FILES_TO_ADD);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllDeflateAndZipStandardEncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.ZIP_STANDARD, null);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllDeflateAndAes128EncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_128);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractAllDeflateAndAes256EncryptionExtractsSuccessfully() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_256);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    zipFile.extractAll(outputFolder.getPath());

    ZipFileVerifier.verifyFolderContentsSameAsSourceFiles(outputFolder);
    verifyNumberOfFilesInOutputFolder(outputFolder, 3);
  }

  @Test
  public void testExtractFileWithFileHeaderWithAes128() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_128);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    FileHeader fileHeader = zipFile.getFileHeader("sample_text_large.txt");
    zipFile.extractFile(fileHeader, outputFolder.getPath());

    File[] outputFiles = outputFolder.listFiles();
    assertThat(outputFiles).hasSize(1);
    ZipFileVerifier.verifyFileContent(TestUtils.getFileFromResources("sample_text_large.txt"), outputFiles[0]);
  }

  @Test
  public void testExtractFileWithFileHeaderWithAes128AndInDirectory() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_128);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFolder(TestUtils.getFileFromResources(""), zipParameters);

    FileHeader fileHeader = zipFile.getFileHeader("test-files/öüäöäö/asöäööl");
    zipFile.extractFile(fileHeader, outputFolder.getPath());

    File outputFile = getFileWithNameFrom(outputFolder, "asöäööl");
    ZipFileVerifier.verifyFileContent(TestUtils.getFileFromResources("öüäöäö/asöäööl"), outputFile);
  }

  @Test
  public void testExtractFileWithFileHeaderWithAes256AndWithANewFileName() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.AES, AesKeyStrength.KEY_STRENGTH_256);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD, zipParameters);

    String newFileName = "newFileName";
    FileHeader fileHeader = zipFile.getFileHeader("sample_text_large.txt");
    zipFile.extractFile(fileHeader, outputFolder.getPath(), newFileName);

    File outputFile = getFileWithNameFrom(outputFolder, newFileName);
    ZipFileVerifier.verifyFileContent(TestUtils.getFileFromResources("sample_text_large.txt"), outputFile);
  }

  @Test
  public void testExtractFileWithFileNameThrowsExceptionWhenFileNotFound() throws ZipException {
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFiles(FILES_TO_ADD);

    expectedException.expect(ZipException.class);
    expectedException.expectMessage("No file found with name NOT_EXISTING in zip file");

    zipFile.extractFile("NOT_EXISTING", outputFolder.getPath());
  }

  @Test
  public void testExtractFileWithFileNameWithZipStandardEncryption() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.ZIP_STANDARD, null);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFolder(TestUtils.getFileFromResources(""), zipParameters);

    zipFile.extractFile("test-files/sample_directory/favicon.ico", outputFolder.getPath());

    File outputFile = getFileWithNameFrom(outputFolder, "favicon.ico");
    ZipFileVerifier.verifyFileContent(TestUtils.getFileFromResources("sample_directory/favicon.ico"), outputFile);
  }

  @Test
  public void testExtractFileWithFileNameWithZipStandardEncryptionAndNewFileName() throws ZipException {
    ZipParameters zipParameters = createZipParameters(EncryptionMethod.ZIP_STANDARD, null);
    ZipFile zipFile = new ZipFile(generatedZipFile, PASSWORD);
    zipFile.addFolder(TestUtils.getFileFromResources(""), zipParameters);

    String newFileName = "newFileName";
    zipFile.extractFile("test-files/sample_directory/favicon.ico", outputFolder.getPath(), newFileName);

    File outputFile = getFileWithNameFrom(outputFolder, newFileName);
    ZipFileVerifier.verifyFileContent(TestUtils.getFileFromResources("sample_directory/favicon.ico"), outputFile);
  }

  private void verifyNumberOfFilesInOutputFolder(File outputFolder, int numberOfExpectedFiles) {
    assertThat(outputFolder.listFiles()).hasSize(numberOfExpectedFiles);
  }

  private File getFileWithNameFrom(File outputFolder, String fileName) throws ZipException {
    List<File> filesInFolder = FileUtils.getFilesInDirectoryRecursive(outputFolder, true);
    Optional<File> file = filesInFolder.stream().filter(e -> e.getName().equals(fileName)).findFirst();
    assertThat(file).isPresent();
    return file.get();
  }

}
