package com.jive.sip.processor.rfc3261.message.impl;

import java.util.Collection;

import com.jive.sip.base.api.RawHeader;
import com.jive.sip.message.api.SipHeaderDefinition;
import com.jive.sip.parsers.api.Parser;
import com.jive.sip.parsers.api.ParserContext;
import com.jive.sip.parsers.api.ParserInput;
import com.jive.sip.parsers.core.ByteParserInput;
import com.jive.sip.parsers.core.DefaultParserContext;
import com.jive.sip.parsers.core.ParserUtils;
import com.jive.sip.parsers.core.ValueHolder;

/**
 * A single value, single field header definition.
 * 
 * @author theo
 * 
 * @param <T>
 */

public class MultiHeaderDefinition<T> extends BaseHeaderDefinition implements SipHeaderDefinition<T>
{

  private Parser<T> parser;

  public MultiHeaderDefinition(final Parser<T> parser, final String name, final Character sname)
  {
    super(name, sname);
  }

  public static <T> SipHeaderDefinition<T> create(final Parser<T> parser, final String name, final Character sname)
  {
    return new MultiHeaderDefinition<T>(parser, name, sname);
  }

  /**
   * Creates a SingleHeaderDefinition which accepts any string.
   * 
   * @param name
   * @param sname
   * @return
   */

  public static SipHeaderDefinition<String> create(final String name, final Character sname)
  {
    return new MultiHeaderDefinition<String>(null, name, sname);
  }

  public static SipHeaderDefinition<String> create(final String name)
  {
    return new MultiHeaderDefinition<String>(null, name, null);
  }

  public static <T> SipHeaderDefinition<T> create(final Parser<T> parser, final String name)
  {
    return new MultiHeaderDefinition<T>(parser, name, null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T parse(final Collection<RawHeader> headers)
  {

    for (final RawHeader header : headers)
    {

      if (this.matches(header.getName()))
      {

        if (this.parser == null)
        {
          return (T) header.getValue();
        }

        final ParserInput input = ByteParserInput.fromString(header.getValue());
        final ParserContext ctx = new DefaultParserContext(input);
        final ValueHolder<T> holder = new ValueHolder<T>();

        if (!this.parser.find(ctx, holder))
        {
          return null;
        }

        do
        {
          this.parser.find(ctx, null);
        }
        while (ctx.skip(ParserUtils.COMMA));

        if (ctx.remaining() > 0)
        {
          return null;
        }

        return holder.value();

      }

    }

    return null;

  }

}
