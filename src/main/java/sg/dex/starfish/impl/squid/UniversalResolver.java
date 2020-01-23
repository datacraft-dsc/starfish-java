package sg.dex.starfish.impl.squid;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.util.DID;

import java.io.IOException;

/**
 * Class encapsulating all possible resolvers
 * and automatically deciding which one to choose.
 *
 * @author Ilya
 */
public class UniversalResolver implements Resolver {
    private DexResolver dexResolver;
    private SquidResolverImpl squidResolverImpl;
    private static final String DEX_METHOD = "dex";

    /**
     * Create UniversalResolver
     *
     * @param DexResolver dexResolver
     * @param SquidResolverImpl squidResolverImpl
     */
    private UniversalResolver(DexResolver dexResolver, SquidResolverImpl squidResolverImpl)  {
        this.dexResolver = dexResolver;
        this.squidResolverImpl = squidResolverImpl;
    }

    /**
     * Creates UniversalResolver
     *
     * @param String squidConfigFile. Squid config file which is used to initialize Squid Resolver.
     *
     * @throws IOException
     * @return UniversalResolver The newly created UniversalResolver
     */
    public static UniversalResolver create(String squidConfigFile) throws IOException {
        SquidService squidService = SquidService.create(squidConfigFile);
        return new UniversalResolver(DexResolver.create(), new SquidResolverImpl(squidService));
    }

    /**
     * Creates UniversalResolver
     *
     * @throws IOException
     * @return UniversalResolver The newly created UniversalResolver
     */
    public static UniversalResolver create() throws IOException {
        return new UniversalResolver(DexResolver.create(), null);
    }

    @Override
    public String getDDOString(DID did) throws ResolverException {
        if(did.getMethod() == DEX_METHOD) {
            return this.dexResolver.getDDOString(did);
        } else {
            if (squidResolverImpl == null) {
                throw new ResolverException("Config for Ocean Protocol type of DID must be provided");
            }
            return this.squidResolverImpl.getDDOString(did);
        }
    }

    @Override
    public void registerDID(DID did, String ddo) throws ResolverException {
        if(did.getMethod() == DEX_METHOD) {
            this.dexResolver.registerDID(did,ddo);
        } else {
            if (squidResolverImpl == null) {
                throw new ResolverException("Config for Ocean Protocol type of DID must be provided");
            }
            this.squidResolverImpl.registerDID(did,ddo);
        }
    }
}