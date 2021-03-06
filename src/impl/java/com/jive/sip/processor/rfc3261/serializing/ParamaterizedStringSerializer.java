package com.jive.sip.processor.rfc3261.serializing;

import java.io.IOException;
import java.io.Writer;

import com.jive.sip.message.api.headers.ParameterizedString;
import com.jive.sip.processor.rfc3261.serializing.serializers.AbstractRfcSerializer;
import com.jive.sip.processor.rfc3261.serializing.serializers.RfcSerializationConstants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParamaterizedStringSerializer extends AbstractRfcSerializer<ParameterizedString>
{

  private final RfcSerializerManager manager;

  @Override
  public void serialize(final Writer w, final ParameterizedString obj) throws IOException
  {

    w.append(obj.getValue());

    if (obj.getParameters().isPresent())
    {
      w.append(RfcSerializationConstants.SEMI);
      this.manager.serializeCollection(w, obj.getParameters().get().getRawParameters(), RfcSerializationConstants.SEMI);
    }

  }

}
