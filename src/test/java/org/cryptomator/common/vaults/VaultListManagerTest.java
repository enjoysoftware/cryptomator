package org.cryptomator.common.vaults;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.cryptomator.common.Constants.MASTERKEY_FILENAME;
import static org.cryptomator.common.Constants.VAULTCONFIG_FILENAME;
import static org.cryptomator.cryptofs.common.Constants.DATA_DIR_NAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VaultListManagerTest {

	@Test
	void testAssertIsVaultDirectoryWhenDataDirIsMissing(@TempDir Path tmpDir) {
		NoSuchFileException e = assertThrows(NoSuchFileException.class, () -> {
			VaultListManager.assertIsVaultDirectory(tmpDir);
		});

		assertTrue(e.getReason().contains(DATA_DIR_NAME + " directory is missing"));
	}

	@Test
	void testAssertIsVaultDirectoryWhenDataDirIsFile(@TempDir Path tmpDir) throws IOException {
		Files.createFile(tmpDir.resolve(DATA_DIR_NAME));

		NoSuchFileException e = assertThrows(NoSuchFileException.class, () -> {
			VaultListManager.assertIsVaultDirectory(tmpDir);
		});

		assertTrue(e.getReason().contains(DATA_DIR_NAME + " is not a directory"));
	}

	@Test
	void testAssertIsVaultDirectoryWhenVaultConfigAndMasterkeyAreMissing(@TempDir Path tmpDir) throws IOException {
		Files.createDirectory(tmpDir.resolve(DATA_DIR_NAME));

		NoSuchFileException e = assertThrows(NoSuchFileException.class, () -> {
			VaultListManager.assertIsVaultDirectory(tmpDir);
		});

		assertTrue(e.getReason().contains(VAULTCONFIG_FILENAME + " is missing"));
		assertTrue(e.getReason().contains(MASTERKEY_FILENAME + " is missing"));
	}

	@Test
	void testAssertIsVaultDirectoryAcceptsModernVault(@TempDir Path tmpDir) throws IOException {
		Files.createDirectory(tmpDir.resolve(DATA_DIR_NAME));
		Files.createFile(tmpDir.resolve(VAULTCONFIG_FILENAME));

		assertDoesNotThrow(() -> {
			VaultListManager.assertIsVaultDirectory(tmpDir);
		});
	}

	@Test
	void testAssertIsVaultDirectoryAcceptsLegacyVaultCandidate(@TempDir Path tmpDir) throws IOException {
		Files.createDirectory(tmpDir.resolve(DATA_DIR_NAME));
		Files.createFile(tmpDir.resolve(MASTERKEY_FILENAME));

		assertDoesNotThrow(() -> {
			VaultListManager.assertIsVaultDirectory(tmpDir);
		});
	}

}
