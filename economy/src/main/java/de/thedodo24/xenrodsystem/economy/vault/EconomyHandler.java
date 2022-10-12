package de.thedodo24.xenrodsystem.economy.vault;

import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.economy.EconomyModule;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;
import java.util.UUID;

public class EconomyHandler extends AbstractEconomy {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "XenrodSystem-Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        if(v == 1)
            return v + " " + Language.getLanguage().get("money-value-one");
        return v + " " + Language.getLanguage().get("money-value");
    }

    @Override
    public String currencyNamePlural() {
        return Language.getLanguage().get("money-value");
    }

    @Override
    public String currencyNameSingular() {
        return Language.getLanguage().get("money-value-one");
    }

    @Override
    public boolean hasAccount(String s) {
        return hasAccount(s, null);
    }

    @Override
    public boolean hasAccount(String s, String world) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        return m != null;
    }

    @Override
    public double getBalance(String s) {
        return getBalance(s, null);
    }

    @Override
    public double getBalance(String s, String world) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return 0;
        }
        return m.getBalance();
    }

    @Override
    public boolean has(String s, double v) {
        return has(s, null, v);
    }

    @Override
    public boolean has(String s, String world, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return false;
        }
        return m.getBalance() >= v;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(s, null, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String world, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Could not found a account for player " + s);
        }
        if(v <= 0)
            v *= -1;
        return new EconomyResponse(-v, m.withdrawMoney((long) v), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return depositPlayer(s, null, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String world, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Could not found a account for player " + s);
        }
        if(v <= 0)
            v *= -1;
        return new EconomyResponse(v, m.depositMoney((long) v), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String accountName, String playerName) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(playerName.toLowerCase());
        return new EconomyResponse(0, m.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No bank support");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Could not find player " + s);
        }
        return new EconomyResponse(0, m.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Could not find player " + s);
        }
        if(m.getBalance() >= v)
            return new EconomyResponse(0, m.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
        return new EconomyResponse(0, m.getBalance(), EconomyResponse.ResponseType.FAILURE, "Player does not have enough money");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Could not found a account for player " + s);
        }
        if(v <= 0)
            v *= -1;
        return new EconomyResponse(-v, m.withdrawMoney((long) v), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Could not found a account for player " + s);
        }
        if(v <= 0)
            v *= -1;
        return new EconomyResponse(v, m.depositMoney((long) v), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse isBankOwner(String accountName, String playerName) {
        return new EconomyResponse(0,0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No bank suppport");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0,0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No bank suppport");
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return createPlayerAccount(s, null);
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        if(hasAccount(s)) {
            return false;
        }
        User m = EconomyModule.getInstance().getManager().getUserManager().getByName(s.toLowerCase());
        if(m == null) {
            m = EconomyModule.getInstance().getManager().getUserManager().register(UUID.randomUUID());
            m.setName(s.toLowerCase());
        }
        return true;
    }
}
