package com.example.denis.CryptocurrencyAPI;

import org.bitcoinj.core.Utils;
import org.bitcoinj.params.AbstractBitcoinNetParams;

public class LitecoinNetwork extends AbstractBitcoinNetParams {
	public LitecoinNetwork() {
		super();
		interval = INTERVAL;
		targetTimespan = TARGET_TIMESPAN;
		maxTarget = Utils.decodeCompactBits(0x1d00ffffL);
		port = 18333;
		addressHeader = 48;
		p2shHeader = 5;
		acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
		dumpedPrivateKeyHeader = 176;
		genesisBlock.setTime(1296688602L);
		genesisBlock.setDifficultyTarget(0x1d00ffffL);
		genesisBlock.setNonce(414098458);
		spendableCoinbaseDepth = 100;
		subsidyDecreaseBlockCount = 210000;
		String genesisHash = genesisBlock.getHashAsString();
	}

	private static LitecoinNetwork instance;
	public static synchronized LitecoinNetwork get() {
		if (instance == null) {
			instance = new LitecoinNetwork();
		}
		return instance;
	}
	@Override
	public String getPaymentProtocolId() {
		return PAYMENT_PROTOCOL_ID_MAINNET;
	}
}
